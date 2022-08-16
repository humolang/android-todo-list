package com.coriolang.todolist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.todolist.data.model.Importance
import com.coriolang.todolist.data.model.TodoItem
import com.coriolang.todolist.data.repository.TodoItemRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val repository: TodoItemRepository
    ) : ViewModel() {

    val todoItems: Flow<List<TodoItem>>
        get() = repository.todoItems

    val todoText: StateFlow<String>
        get() = repository.todoText

    val todoDeadline: StateFlow<Long>
        get() = repository.todoDeadline

    val todoImportance: StateFlow<Importance>
        get() = repository.todoImportance

    fun insertTodoItem() {
        viewModelScope.launch {
            repository.insertTodoItem()
        }
    }

    fun updateTodoItem(id: String) {
        viewModelScope.launch {
            repository.updateTodoItem(id)
        }
    }

    fun deleteTodoItem(id: String) {
        viewModelScope.launch {
            repository.deleteTodoItem(id)
        }
    }

    fun setTodoText(text: String) = repository.setTodoText(text)
    fun setTodoDeadline(deadline: Long) = repository.setTodoDeadline(deadline)
    fun setTodoImportance(importance: Importance) = repository.setTodoImportance(importance)

    fun setTodoIsCompleted(id: String, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.setTodoIsCompleted(id, isCompleted)
        }
    }
}