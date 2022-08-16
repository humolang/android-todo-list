package com.coriolang.todolist.data.model

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "todo_items")
data class TodoItem(

    @PrimaryKey
    val id: String = "",

    @NonNull
    val text: String = "",

    @NonNull
    val importance: Importance = Importance.NORMAL,

    @NonNull @ColumnInfo(name = "is_completed")
    val isCompleted: Boolean = false,

    @NonNull @ColumnInfo(name = "creation_date")
    val creationDate: Long = 0L,

    @ColumnInfo(name = "deadline_date")
    val deadlineDate: Long = 0L,

    @ColumnInfo(name = "modification_date")
    val modificationDate: Long = 0L
)
