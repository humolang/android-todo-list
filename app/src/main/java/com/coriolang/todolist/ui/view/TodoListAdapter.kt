package com.coriolang.todolist.ui.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.coriolang.todolist.R
import com.coriolang.todolist.data.model.Importance
import com.coriolang.todolist.data.model.TodoItem
import com.coriolang.todolist.databinding.ItemTodoBinding
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoListAdapter(
    private val viewModel: TodoListViewModel
    ) : ListAdapter<TodoItem,
        TodoListAdapter.TodoItemViewHolder>(DiffCallback) {

    class TodoItemViewHolder(
        private val context: Context,
        private val binding: ItemTodoBinding,
        private val viewModel: TodoListViewModel
        ) : RecyclerView.ViewHolder(binding.root) {

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

            binding.apply {
                checkBoxTodo.isChecked = todoItem.isCompleted
                checkBoxTodo.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.setTodoIsCompleted(todoItem.id, isChecked)
                }

                imageViewImportance.setImageResource(imageImportance)
                imageViewImportance.contentDescription = context.getString(contentDescription)

                textViewTodo.text = todoItem.text
            }

            itemView.setOnClickListener {
                val action = TodoListFragmentDirections
                    .actionTodoListFragmentToTodoEditFragment(id = todoItem.id)
                it.findNavController().navigate(action)
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
            ),
            viewModel
        )
    }

    override fun onBindViewHolder(holder: TodoItemViewHolder, position: Int) {
        val todoItem = getItem(position)
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