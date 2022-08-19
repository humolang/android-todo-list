package com.coriolang.todolist

import android.app.Application
import android.content.Context
import com.coriolang.todolist.ioc.ApplicationComponent

class TodoApplication : Application() {

    val applicationComponent by lazy { ApplicationComponent(this) }

    companion object {
        fun get(context: Context) =
            context.applicationContext as TodoApplication
    }
}