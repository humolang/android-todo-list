package com.coriolang.todolist.data.repository

import com.coriolang.todolist.data.datasource.TodoApi
import com.coriolang.todolist.data.datasource.TodoItemDao
import com.coriolang.todolist.data.model.Importance
import com.coriolang.todolist.data.model.TodoItem
import com.coriolang.todolist.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class TodoItemRepository(
    private val todoItemDao: TodoItemDao,
    private val todoApi: TodoApi
    ) {

    val todoItems: Flow<List<TodoItem>>
        get() = todoItemDao.getObservableList()

    private val _todoText = MutableStateFlow("")
    val todoText = _todoText.asStateFlow()

    private val _todoDeadline = MutableStateFlow(0L)
    val todoDeadline = _todoDeadline.asStateFlow()

    private val _todoImportance = MutableStateFlow(Importance.NORMAL)
    val todoImportance = _todoImportance.asStateFlow()

    suspend fun getTodoList() {
        withContext(Dispatchers.IO) {
            val localList = todoItemDao.findAll()
            for (todoItem in localList) {
                launch { todoItemDao.delete(todoItem) }
            }

            val networkList = todoApi.getListRequest()
            for (todoItem in networkList) {
                launch { todoItemDao.insert(todoItem) }
            }
        }
    }

    suspend fun refreshTodoList() {
        withContext(Dispatchers.IO) {
            val list = todoItemDao.findAll()
            val patchedList = todoApi.patchListRequest(list)

            for (networkTodoItem in patchedList) {
                launch {
                    val localTodoItem = todoItemDao
                        .findById(networkTodoItem.id)

                    if (localTodoItem.modificationDate
                        < networkTodoItem.modificationDate) {

                        todoItemDao.update(networkTodoItem)
                    }
                }
            }
        }
    }

    suspend fun insertTodoItem() {
        val currentTime = System.currentTimeMillis()
        val todoItem = TodoItem(
            id = UUID.randomUUID().toString(),
            text = todoText.value,
            importance = todoImportance.value,
            isCompleted = false,
            creationDate = currentTime,
            deadlineDate = todoDeadline.value,
            modificationDate = currentTime
        )

        withContext(Dispatchers.IO) {
            launch { todoItemDao.insert(todoItem) }
            launch { todoApi.postElementRequest(todoItem) }
        }
    }

    suspend fun updateTodoItem(id: String) {
        withContext(Dispatchers.IO) {
            val todoItem = todoItemDao.findById(id)
            val newTodoItem = todoItem.copy(
                text = todoText.value,
                importance = todoImportance.value,
                deadlineDate = todoDeadline.value,
                modificationDate = System.currentTimeMillis()
            )

            launch { todoItemDao.update(newTodoItem) }
            launch { todoApi.putElementRequest(newTodoItem.id, newTodoItem) }
        }
    }

    suspend fun deleteTodoItem(id: String) {
        withContext(Dispatchers.IO) {
            val todoItem = todoItemDao.findById(id)

            launch { todoItemDao.delete(todoItem) }
            launch { todoApi.deleteElementRequest(todoItem.id) }
        }
    }

    suspend fun findTodoItemById(id: String): TodoItem {
        val todoItem = withContext(Dispatchers.IO) {
            todoItemDao.findById(id)
        }

        return todoItem
    }

    fun setTodoText(text: String) = _todoText.update { text }
    fun setTodoDeadline(deadline: Long) = _todoDeadline.update { deadline }
    fun setTodoImportance(importance: Importance) = _todoImportance.update { importance }

    suspend fun setTodoIsCompleted(id: String, isCompleted: Boolean) {
        withContext(Dispatchers.IO) {
            val todoItem = todoItemDao.findById(id)
            val newTodoItem = todoItem.copy(
                isCompleted = isCompleted,
                modificationDate = System.currentTimeMillis()
            )

            launch { todoItemDao.update(newTodoItem) }
            launch { todoApi.putElementRequest(newTodoItem.id, newTodoItem) }
        }
    }

    suspend fun registerUser(username: String, password: String) {
        val user = User(username, password)
        todoApi.registrationRequest(user)
    }

    suspend fun loginUser(username: String, password: String) {
        val user = User(username, password)
        todoApi.loginRequest(user)
    }
}