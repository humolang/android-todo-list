package com.coriolang.todolist

import android.app.Application
import com.coriolang.todolist.data.TodoDatabase

class TodoApplication : Application() {

    val database: TodoDatabase by lazy { TodoDatabase.getDatabase(this) }
}