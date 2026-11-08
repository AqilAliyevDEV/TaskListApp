package com.aqilaliyev.tasklist

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/** Saves the task list on the device so it survives closing the app. */
class TaskStore(context: Context) {

    private val prefs = context.getSharedPreferences("tasks", Context.MODE_PRIVATE)

    fun load(): MutableList<Task> {
        val raw = prefs.getString(KEY, null) ?: return mutableListOf()
        val array = JSONArray(raw)
        val result = mutableListOf<Task>()
        for (i in 0 until array.length()) {
            val item = array.getJSONObject(i)
            result.add(
                Task(
                    id = item.getLong("id"),
                    title = item.getString("title"),
                    done = item.getBoolean("done")
                )
            )
        }
        return result
    }

    fun save(tasks: List<Task>) {
        val array = JSONArray()
        tasks.forEach { task ->
            array.put(
                JSONObject()
                    .put("id", task.id)
                    .put("title", task.title)
                    .put("done", task.done)
            )
        }
        prefs.edit().putString(KEY, array.toString()).apply()
    }

    private companion object {
        const val KEY = "list"
    }
}
