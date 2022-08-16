package com.coriolang.todolist

import android.app.Application
import com.coriolang.todolist.data.datasource.TodoDatabase
import com.coriolang.todolist.data.repository.TodoItemRepository

class TodoApplication : Application() {

    private val database: TodoDatabase by lazy { TodoDatabase.getDatabase(this) }
    val repository: TodoItemRepository by lazy { TodoItemRepository(database.todoItemDao()) }
}