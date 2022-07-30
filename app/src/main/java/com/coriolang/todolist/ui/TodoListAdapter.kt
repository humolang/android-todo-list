package com.coriolang.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coriolang.todolist.R
import com.coriolang.todolist.databinding.ItemTodoBinding
import com.coriolang.todolist.model.Importance
import com.coriolang.todolist.model.TodoItem

class TodoListAdapter :
    ListAdapter<TodoItem, TodoListAdapter.TodoItemViewHolder>(DiffCallback) {

    class TodoItemViewHolder(private var binding: ItemTodoBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(todoItem: TodoItem) {
            val imageImportance = when (todoItem.importance) {
                Importance.LOW -> R.drawable.ic_low_importance
                Importance.NORMAL -> R.drawable.ic_normal_importance
                Importance.HIGH -> R.drawable.ic_high_importance
            }

            binding.apply {
                checkBoxTodo.isChecked = todoItem.isCompleted
                checkBoxTodo.text = todoItem.text

                imageViewImportance.setImageResource(imageImportance)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        val binding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return TodoItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TodoItem>() {
            override fun areItemsTheSame(
                oldItem: TodoItem,
                newItem: TodoItem
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TodoItem,
                newItem: TodoItem
            ): Boolean = oldItem == newItem
        }
    }
}