package com.coriolang.todolist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.todolist.data.datasource.RequestException
import com.coriolang.todolist.data.model.Importance
import com.coriolang.todolist.data.model.TodoItem
import com.coriolang.todolist.data.repository.TodoItemRepository
import com.coriolang.todolist.ui.NAVIGATE_TO_LIST
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
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

    private val _exceptionMessage = MutableStateFlow("")
    val exceptionMessage = _exceptionMessage.asStateFlow()

    private val handler = CoroutineExceptionHandler { _, exception ->
        if (exception is RequestException) {
            _exceptionMessage.update {
                exception.message
            }

            _exceptionMessage.update { "" }
        } else {
            throw exception
        }
    }

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

    fun registerUser(username: String, password: String) {
        viewModelScope.launch(handler) {
            repository.registerUser(username, password)

            _exceptionMessage.update { NAVIGATE_TO_LIST }
            _exceptionMessage.update { "" }
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch(handler) {
            repository.loginUser(username, password)

            _exceptionMessage.update { NAVIGATE_TO_LIST }
            _exceptionMessage.update { "" }
        }
    }
}