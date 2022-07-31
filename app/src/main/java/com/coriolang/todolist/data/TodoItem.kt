package com.coriolang.todolist.data

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_items")
data class TodoItem(
    @PrimaryKey val id: Int,
    @NonNull val text: String,
    @NonNull val importance: Importance,
    @NonNull @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    @NonNull @ColumnInfo(name = "creation_date") val creationDate: Long,
    @ColumnInfo(name = "deadline_date") val deadlineDate: Long?,
    @ColumnInfo(name = "modification_date") val modificationDate: Long?
)
