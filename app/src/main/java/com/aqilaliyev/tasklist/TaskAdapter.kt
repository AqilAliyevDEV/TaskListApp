package com.aqilaliyev.tasklist

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val onToggle: (Task) -> Unit,
    private val onDelete: (Task) -> Unit
) : ListAdapter<Task, TaskAdapter.ViewHolder>(Diff) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val check: CheckBox = view.findViewById(R.id.check)
        val delete: ImageButton = view.findViewById(R.id.delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val task = getItem(position)

        // Remove the old listener first so setting isChecked does not trigger it.
        holder.check.setOnCheckedChangeListener(null)
        holder.check.text = task.title
        holder.check.isChecked = task.done
        holder.check.paintFlags = if (task.done) {
            holder.check.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.check.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        holder.check.setOnCheckedChangeListener { _, _ -> onToggle(task) }
        holder.delete.setOnClickListener { onDelete(task) }
    }

    private object Diff : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}
