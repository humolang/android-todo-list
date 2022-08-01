package com.coriolang.todolist.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coriolang.todolist.data.todoItem.TodoItem
import com.coriolang.todolist.data.todoItem.TodoItemDao

@Database(entities = arrayOf(TodoItem::class), version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun todoItemDao(): TodoItemDao

    companion object {

        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }

//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context,
//                    TodoDatabase::class.java,
//                    "todo_database")
//                    .fallbackToDestructiveMigration()
//                    .build()
//                INSTANCE = instance
//
//                instance
//            }
        }
    }
}