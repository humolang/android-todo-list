package com.coriolang.todolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.todolist.data.todoItem.Importance
import com.coriolang.todolist.data.todoItem.TodoItem
import com.coriolang.todolist.data.todoItem.TodoItemDao
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoListViewModel(private val todoItemDao: TodoItemDao) : ViewModel() {

    fun insertTodoItem(text: String, importance: Importance) {
        val todoItem = TodoItem(
            text = text,
            importance = importance,
            isCompleted = false
        )

        viewModelScope.launch {
            todoItemDao.insert(todoItem)
        }
    }

    fun insertTodoItem(text: String, importance: Importance, deadlineDate: Long) {
        val todoItem = TodoItem(
            text = text,
            importance = importance,
            isCompleted = false,
            deadlineDate = deadlineDate
        )

        viewModelScope.launch {
            todoItemDao.insert(todoItem)
        }
    }

    fun updateTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemDao.update(todoItem)
        }
    }

    fun deleteTodoItem(todoItem: TodoItem) {
        viewModelScope.launch {
            todoItemDao.delete(todoItem)
        }
    }

    suspend fun findTodoItemById(id: Int): TodoItem {
        return viewModelScope.async {
            todoItemDao.findById(id)
        }.await()
    }

    fun allTodoItems(): Flow<List<TodoItem>> = todoItemDao.findAll()
}