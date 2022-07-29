package com.coriolang.todolist.model

class TodoItemsRepository {

    private val _todoItems = mutableListOf<TodoItem>()
    val todoItems: List<TodoItem>
        get() = _todoItems

    fun addItem(todoItem: TodoItem) {
        _todoItems.add(todoItem)
    }
}