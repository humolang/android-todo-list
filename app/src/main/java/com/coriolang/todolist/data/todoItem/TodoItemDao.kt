package com.coriolang.todolist.data.todoItem

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg todoItems: TodoItem)

    @Query("select * from todo_items order by creation_date asc")
    fun findAll(): Flow<List<TodoItem>>
}