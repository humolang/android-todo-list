package com.coriolang.todolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.todolist.data.todoItem.Importance
import com.coriolang.todolist.data.todoItem.TodoItem
import com.coriolang.todolist.data.todoItem.TodoItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoListViewModel(private val todoItemDao: TodoItemDao) : ViewModel() {

    private val _todoItem = MutableStateFlow(TodoItem())
    val todoItem: StateFlow<TodoItem> = _todoItem

    fun insertTodoItem() {
        val currentTime = System.currentTimeMillis()

        _todoItem.value = _todoItem.value.copy(
            creationDate = currentTime,
            modificationDate = currentTime
        )

        viewModelScope.launch {
            todoItemDao.insert(todoItem.value)
        }
    }

    fun updateTodoItem() {
        _todoItem.value = _todoItem.value.copy(
            modificationDate = System.currentTimeMillis()
        )

        viewModelScope.launch {
            todoItemDao.update(todoItem.value)
        }
    }

    fun deleteTodoItem() {
        viewModelScope.launch {
            todoItemDao.delete(todoItem.value)
        }
    }

    fun findTodoItemById(id: Int) {
        viewModelScope.launch {
            _todoItem.value = todoItemDao.findById(id)
        }
    }

    fun allTodoItems(): Flow<List<TodoItem>> = todoItemDao.findAll()

    fun defaultTodoItem() {
        _todoItem.value = TodoItem()
    }

    fun setTodoText(text: String) {
        _todoItem.value = _todoItem.value.copy(
            text = text
        )
    }

    fun setTodoImportance(importance: Importance) {
        _todoItem.value = _todoItem.value.copy(
            importance = importance
        )
    }

    fun setTodoIsCompleted(id: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            var todoItem = todoItemDao.findById(id)
            todoItem = todoItem.copy(
                isCompleted = isCompleted,
                modificationDate = System.currentTimeMillis()
            )

            todoItemDao.update(todoItem)
        }
    }

    fun setTodoDeadlineDate(deadlineDate: Long) {
        _todoItem.value = _todoItem.value.copy(
            deadlineDate = deadlineDate
        )
    }
}