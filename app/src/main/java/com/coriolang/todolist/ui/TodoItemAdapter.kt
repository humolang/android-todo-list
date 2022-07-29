package com.coriolang.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coriolang.todolist.R
import com.coriolang.todolist.databinding.ItemTodoBinding
import com.coriolang.todolist.model.Importance
import com.coriolang.todolist.model.TodoItem

class TodoItemAdapter(private val todoItems: List<TodoItem>) :
    RecyclerView.Adapter<TodoItemAdapter.ViewHolder>() {

    class ViewHolder(itemTodoBinding: ItemTodoBinding)
        : RecyclerView.ViewHolder(itemTodoBinding.root) {

        private var _binding: ItemTodoBinding? = null
        val binding
            get() = _binding!!

        init {
            _binding = itemTodoBinding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemTodoBinding = ItemTodoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(itemTodoBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todoItem = todoItems[position]

        val imageImportance = when (todoItem.importance) {
            Importance.LOW -> R.drawable.ic_low_importance
            Importance.NORMAL -> R.drawable.ic_normal_importance
            Importance.HIGH -> R.drawable.ic_high_importance
        }

        holder.binding.apply {
            checkBoxTodo.isChecked = todoItem.isCompleted
            checkBoxTodo.text = todoItem.text

            imageViewImportance.setImageResource(imageImportance)
        }
    }

    override fun getItemCount() = todoItems.size
}