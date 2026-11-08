package com.aqilaliyev.tasklist

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var store: TaskStore
    private lateinit var adapter: TaskAdapter
    private lateinit var countView: TextView
    private lateinit var emptyView: TextView
    private lateinit var input: EditText

    private val tasks = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        store = TaskStore(this)
        tasks.addAll(store.load())

        countView = findViewById(R.id.count)
        emptyView = findViewById(R.id.empty)
        input = findViewById(R.id.input)

        adapter = TaskAdapter(onToggle = ::toggle, onDelete = ::delete)
        val list = findViewById<RecyclerView>(R.id.list)
        list.layoutManager = LinearLayoutManager(this)
        list.adapter = adapter

        findViewById<Button>(R.id.add).setOnClickListener { addTask() }
        input.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addTask()
                true
            } else {
                false
            }
        }

        render()
    }

    private fun addTask() {
        val title = input.text.toString().trim()
        if (title.isEmpty()) return
        tasks.add(Task(id = System.currentTimeMillis(), title = title, done = false))
        input.text.clear()
        persist()
    }

    private fun toggle(task: Task) {
        val index = tasks.indexOfFirst { it.id == task.id }
        if (index >= 0) {
            tasks[index] = tasks[index].copy(done = !tasks[index].done)
            persist()
        }
    }

    private fun delete(task: Task) {
        tasks.removeAll { it.id == task.id }
        persist()
    }

    private fun persist() {
        store.save(tasks)
        render()
    }

    private fun render() {
        adapter.submitList(tasks.toList())
        countView.text = getString(R.string.count, tasks.count { it.done }, tasks.size)
        emptyView.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
    }
}
