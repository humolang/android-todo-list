package com.coriolang.todolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coriolang.todolist.data.TodoItemDao

class TodoListViewModelFactory(
    private val todoItemDao: TodoItemDao
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            return TodoListViewModel(todoItemDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}