package com.coriolang.todolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coriolang.todolist.data.todoItem.TodoItemDao

class TodoListViewModelFactory(
    private val todoItemDao: TodoItemDao
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoListViewModel(todoItemDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}