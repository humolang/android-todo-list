package com.coriolang.todolist.viewmodels

import androidx.lifecycle.ViewModel
import com.coriolang.todolist.data.TodoItem
import com.coriolang.todolist.data.TodoItemDao

class TodoListViewModel(private val todoItemDao: TodoItemDao) : ViewModel() {

    fun allTodoItems(): List<TodoItem> = todoItemDao.getAll()
}