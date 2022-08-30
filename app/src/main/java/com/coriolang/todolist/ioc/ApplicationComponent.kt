package com.coriolang.todolist.ioc

import android.content.Context
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.data.datasource.TodoApi
import com.coriolang.todolist.data.datasource.TodoDatabase
import com.coriolang.todolist.data.repository.TodoItemRepository

class ApplicationComponent(context: Context) {

    private val database = TodoDatabase.getDatabase(TodoApplication.get(context))
    private val api = TodoApi()

    private val repository = TodoItemRepository(
        database.todoItemDao(),
        api
    )

    val viewModelFactory = TodoListViewModelFactory(repository)
}