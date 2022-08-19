package com.coriolang.todolist.ioc

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.coriolang.todolist.data.repository.TodoItemRepository
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoListViewModelFactory(
    private val repository: TodoItemRepository
    ) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoListViewModel(repository) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}