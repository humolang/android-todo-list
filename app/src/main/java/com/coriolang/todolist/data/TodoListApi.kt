package com.coriolang.todolist.data

import com.coriolang.todolist.data.todoItem.TodoItem
import com.coriolang.todolist.data.user.User
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class TodoListApi {

    companion object {
        const val BASE_URL = "http://10.0.2.2:8080"
    }

    private val client = HttpClient(OkHttp)
    private var token = ""

    suspend fun registrationRequest(user: User) {
        val jsonUser = Json.encodeToString(user)

        withContext(Dispatchers.IO) {
            client.post("$BASE_URL/registration") {
                contentType(ContentType.Application.Json)
                setBody(jsonUser)
            }
        }
    }

    suspend fun loginRequest(user: User) {
        val jsonUser = Json.encodeToString(user)

        val response = withContext(Dispatchers.IO) {
             client.post("$BASE_URL/login") {
                contentType(ContentType.Application.Json)
                setBody(jsonUser)
            }
        }

        val jsonHashMap = response.body<String>()
        val hashMap = Json
            .decodeFromString<HashMap<String, String>>(jsonHashMap)

        token = hashMap["token"] ?: ""
    }

    suspend fun getListRequest(): List<TodoItem> {
        val response = withContext(Dispatchers.IO) {
            client.get("$BASE_URL/list") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

        val jsonList = response.body<String>()
        val list = Json
            .decodeFromString<List<TodoItem>>(jsonList)

        return list
    }

    suspend fun patchListRequest(todoItems: List<TodoItem>): List<TodoItem> {
        val jsonTodoItems = Json.encodeToString(todoItems)

        val response = withContext(Dispatchers.IO) {
            client.patch("$BASE_URL/list") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }

                contentType(ContentType.Application.Json)
                setBody(jsonTodoItems)
            }
        }

        val jsonList = response.body<String>()
        val list = Json
            .decodeFromString<List<TodoItem>>(jsonList)

        return list
    }

    suspend fun getElementRequest(id: String): TodoItem {
        val response = withContext(Dispatchers.IO) {
            client.get("$BASE_URL/list/$id") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

        val jsonItem = response.body<String>()
        val todoItem = Json
            .decodeFromString<TodoItem>(jsonItem)

        return todoItem
    }

    suspend fun postElementRequest(todoItem: TodoItem): TodoItem {
        var jsonItem = Json.encodeToString(todoItem)

        val response = withContext(Dispatchers.IO) {
            client.post("$BASE_URL/list") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }

                contentType(ContentType.Application.Json)
                setBody(jsonItem)
            }
        }

        jsonItem = response.body<String>()

        return Json
            .decodeFromString<TodoItem>(jsonItem)
    }

    suspend fun putElementRequest(id: String, todoItem: TodoItem): TodoItem {
        var jsonItem = Json.encodeToString(todoItem)

        val response = withContext(Dispatchers.IO) {
            client.put("$BASE_URL/list/$id") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }

                contentType(ContentType.Application.Json)
                setBody(jsonItem)
            }
        }

        jsonItem = response.body<String>()

        return Json
            .decodeFromString<TodoItem>(jsonItem)
    }

    suspend fun deleteElementRequest(id: String): TodoItem {
        val response = withContext(Dispatchers.IO) {
            client.delete("$BASE_URL/list/$id") {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

        val jsonItem = response.body<String>()
        val todoItem = Json
            .decodeFromString<TodoItem>(jsonItem)

        return todoItem
    }
}