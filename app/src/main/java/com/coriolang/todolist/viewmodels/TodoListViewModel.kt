package com.coriolang.todolist.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.coriolang.todolist.data.todoItem.Importance
import com.coriolang.todolist.data.todoItem.TodoItem
import com.coriolang.todolist.data.todoItem.TodoItemDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class TodoListViewModel(private val todoItemDao: TodoItemDao) : ViewModel() {

    fun insertTodoItem() {
        val todoItem = TodoItem(
            text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
                    "sed do eiusmod tempor incididunt ut labore et dolore magna " +
                    "aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
                    "ullamco laboris nisi ut aliquip ex ea commodo consequat. " +
                    "Duis aute irure dolor in reprehenderit in voluptate velit " +
                    "esse cillum dolore eu fugiat nulla pariatur. Excepteur sint " +
                    "occaecat cupidatat non proident, sunt in culpa qui officia " +
                    "deserunt mollit anim id est laborum.",
            importance = Importance.NORMAL,
            isCompleted = false
        )

        viewModelScope.launch {
            todoItemDao.insert(todoItem)
        }
    }

    fun allTodoItems(): Flow<List<TodoItem>> = todoItemDao.findAll()
}