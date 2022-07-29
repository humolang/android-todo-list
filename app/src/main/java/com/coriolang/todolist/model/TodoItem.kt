package com.coriolang.todolist.model

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadlineDate: Long = 0L,
    val isCompleted: Boolean,
    val creationDate: Long,
    val modificationDate: Long = 0L
)
