package com.coriolang.todolist.data.todoItem

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg todoItems: TodoItem)

    @Query("select * from todo_items order by creation_date asc")
    fun findAll(): Flow<List<TodoItem>>
}