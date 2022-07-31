package com.coriolang.todolist.data

import java.util.*

class TodoItemsRepository {

    private val _todoItems = mutableListOf<TodoItem>(
        TodoItem("1", "text1", Importance.NORMAL, 0L, true, Date().time, 0L),
        TodoItem("2",
            "hey text2text2 text2text2 text2text2 text2text2 text2text2 text2text2 text2text2" +
                    " text2text2 text2text2 text2text2 text2text2 text2text2 text2text2 text2text2" +
                    " text2text2 text2text2 text2text2 text2text2 text2text2 text2text2 text2text2" +
                    " text2text2 text2text2 text2text2 text2text2 text2text2 text2text2 text2text2" +
                    " text2text2 text2text2 text2text2 text2text2 text2text2 text2text2 text2text2" +
                    " text2text2 text2text2 text2text2 text2text2 text2text2 text2text2 text2text2",
            Importance.NORMAL, 0L, false, Date().time, 0L),
        TodoItem("3", "text3", Importance.LOW, 0L, true, Date().time, 0L),
        TodoItem("4", "text4", Importance.LOW, 0L, true, Date().time, 0L),
        TodoItem("5", "text5", Importance.HIGH, 0L, true, Date().time, 0L),
        TodoItem("6", "text6", Importance.HIGH, 0L, false, Date().time, 0L),
        TodoItem("7", "text7", Importance.HIGH, 0L, false, Date().time, 0L),
        TodoItem("8", "text8", Importance.NORMAL, 0L, false, Date().time, 0L),
        TodoItem("9", "text9", Importance.NORMAL, 0L, true, Date().time, 0L),
        TodoItem("10", "text10", Importance.LOW, 0L, false, Date().time, 0L),
        TodoItem("11", "text11", Importance.LOW, 0L, true, Date().time, 0L),
        TodoItem("12", "text12", Importance.NORMAL, 0L, false, Date().time, 0L),
        TodoItem("13", "text13", Importance.HIGH, 0L, true, Date().time, 0L),
        TodoItem("14", "text14", Importance.HIGH, 0L, false, Date().time, 0L),
        TodoItem("15", "text15", Importance.NORMAL, 0L, false, Date().time, 0L),
        TodoItem("16", "text16", Importance.LOW, 0L, true, Date().time, 0L),
        TodoItem("17", "text17", Importance.LOW, 0L, true, Date().time, 0L),
        TodoItem("18", "text18", Importance.NORMAL, 0L, true, Date().time, 0L),
        TodoItem("19", "text19", Importance.HIGH, 0L, true, Date().time, 0L),
        TodoItem("20", "text20", Importance.NORMAL, 0L, false, Date().time, 0L),
    )
    val todoItems: List<TodoItem>
        get() = _todoItems

    fun addItem(todoItem: TodoItem) {
        _todoItems.add(todoItem)
    }
}