package com.coriolang.todolist.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coriolang.todolist.R
import com.coriolang.todolist.data.todoItem.Importance
import com.coriolang.todolist.data.todoItem.TodoItem
import com.coriolang.todolist.databinding.ItemTodoBinding

class TodoListAdapter(
    private val onCheckboxClicked: (String, Boolean) -> Unit,
    private val onTodoItemClicked: (String) -> Unit
) : ListAdapter<TodoItem, TodoListAdapter.TodoItemViewHolder>(DiffCallback) {

    class TodoItemViewHolder(
        private val context: Context,
        private var _binding: ItemTodoBinding
    ) : RecyclerView.ViewHolder(_binding.root) {

        val binding get() = _binding

        fun bind(todoItem: TodoItem) {
            val imageImportance = when (todoItem.importance) {
                Importance.LOW -> R.drawable.ic_low_importance
                Importance.NORMAL -> R.drawable.ic_normal_importance
                Importance.HIGH -> R.drawable.ic_high_importance
            }
            val contentDescription = when (todoItem.importance) {
                Importance.LOW -> R.string.low_importance
                Importance.NORMAL -> R.string.normal_importance
                Importance.HIGH -> R.string.high_importance
            }

            _binding.apply {
                checkBoxTodo.isChecked = todoItem.isCompleted
                textViewTodo.text = todoItem.text
                imageViewImportance.setImageResource(imageImportance)
                imageViewImportance.contentDescription = context.getString(contentDescription)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoItemViewHolder {
        return TodoItemViewHolder(
            parent.context,
            ItemTodoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val todoItem = getItem(position)

        holder.binding.checkBoxTodo.setOnCheckedChangeListener { _, isChecked ->
            onCheckboxClicked(todoItem.id, isChecked)
        }
        holder.itemView.setOnClickListener {
            onTodoItemClicked(todoItem.id)
        }

        holder.bind(todoItem)
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