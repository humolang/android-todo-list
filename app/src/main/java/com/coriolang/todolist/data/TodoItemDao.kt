package com.coriolang.todolist.data

import androidx.room.Dao
import androidx.room.Query

@Dao
interface TodoItemDao {

    @Query("select * from todo_items order by creation_date asc")
    fun getAll(): List<TodoItem>
}