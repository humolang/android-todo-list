package com.coriolang.todolist.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.todolist.exceptions.RequestException
import com.coriolang.todolist.data.model.Importance
import com.coriolang.todolist.data.model.TodoItem
import com.coriolang.todolist.data.repository.TodoItemRepository
import com.coriolang.todolist.OK
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.network.sockets.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
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
        when (exception) {
            is RequestException ->
                setExceptionMessage(exception.message)
            is ConnectTimeoutException -> {
                val message = "You don't have an internet connection or the server is down."
                setExceptionMessage(message)
            }
            else -> throw exception
        }
    }

    private fun setExceptionMessage(message: String) {
        _exceptionMessage.update { message }
        _exceptionMessage.update { "" }
    }

    fun refreshTodoList() {
        viewModelScope.launch(handler) {
            repository.refreshTodoList()
        }
    }

    fun insertTodoItem() {
        viewModelScope.launch(handler) {
            repository.insertTodoItem()
        }
    }

    fun updateTodoItem(id: String) {
        viewModelScope.launch(handler) {
            repository.updateTodoItem(id)
        }
    }

    fun deleteTodoItem(id: String) {
        viewModelScope.launch(handler) {
            repository.deleteTodoItem(id)
        }
    }

    fun findTodoItemById(id: String) {
        val todoItem = runBlocking(handler) {
            repository.findTodoItemById(id)
        }

        // todoItem == null if no such row in database
        if (todoItem != null) {
            repository.setTodoText(todoItem.text)
            repository.setTodoDeadline(todoItem.deadlineDate)
            repository.setTodoImportance(todoItem.importance)
        } else {
            repository.setTodoText("")
            repository.setTodoDeadline(0L)
            repository.setTodoImportance(Importance.NORMAL)
        }
    }

    fun setTodoText(text: String) = repository.setTodoText(text)
    fun setTodoDeadline(deadline: Long) = repository.setTodoDeadline(deadline)
    fun setTodoImportance(importance: Importance) = repository.setTodoImportance(importance)

    fun setTodoIsCompleted(id: String, isCompleted: Boolean) {
        viewModelScope.launch(handler) {
            repository.setTodoIsCompleted(id, isCompleted)
        }
    }

    fun registerUser(username: String, password: String) {
        viewModelScope.launch(handler) {
            repository.registerUser(username, password)
            repository.loginUser(username, password)
            repository.getTodoList()

            _exceptionMessage.update { OK }
            _exceptionMessage.update { "" }
        }
    }

    fun loginUser(username: String, password: String) {
        viewModelScope.launch(handler) {
            repository.loginUser(username, password)
            repository.getTodoList()

            _exceptionMessage.update { OK }
            _exceptionMessage.update { "" }
        }
    }

    fun authorizeUser() {
        runBlocking(handler) {
            repository.authorizeUser()
        }
    }

    fun usernameIsEmpty() = repository.usernameIsEmpty()

    fun clearUsername() {
        repository.clearUsername()
    }

    fun tokenHasExpired() = repository.tokenHasExpired()

    fun clearToken() {
        repository.clearToken()
    }
}