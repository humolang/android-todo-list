package com.coriolang.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(TodoItem::class), version = 1)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao

    companion object {

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    TodoDatabase::class.java,
                    "todo_database")
                    .build()
                INSTANCE = instance

                instance
            }
        }
    }
}