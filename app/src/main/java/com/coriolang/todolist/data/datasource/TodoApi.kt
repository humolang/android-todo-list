package com.coriolang.todolist.data.datasource

import android.content.SharedPreferences
import com.coriolang.todolist.data.model.TodoItem
import com.coriolang.todolist.data.model.User
import com.coriolang.todolist.exceptions.RequestException
import com.coriolang.todolist.TOKEN_KEY
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoApi @Inject constructor(
    private val sharedPreferences: SharedPreferences
    ) {

    companion object {
        const val BASE_URL = "https://10.0.2.2:8443"
    }

    private val client = HttpClient(OkHttp) {
        install(HttpCookies)
    }

    private var token: String?
        get() = sharedPreferences
            .getString(TOKEN_KEY, "")
        set(value) = sharedPreferences.edit()
            .putString(TOKEN_KEY, value).apply()

    suspend fun registrationRequest(user: User) {
        val jsonUser = Json.encodeToString(user)

        val response = withContext(Dispatchers.IO) {
            client.post("$BASE_URL/registration") {
                contentType(ContentType.Application.Json)
                setBody(jsonUser)
            }
        }

        if (response.status != HttpStatusCode.OK) {
            val message = response.bodyAsText()
            throw RequestException(message)
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

        if (response.status != HttpStatusCode.OK) {
            val message = response.bodyAsText()
            throw RequestException(message)
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

        if (response.status != HttpStatusCode.OK) {
            checkUnauthorizedStatus(response)

            val message = response.bodyAsText()
            throw RequestException(message)
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

        if (response.status != HttpStatusCode.OK) {
            checkUnauthorizedStatus(response)

            val message = response.bodyAsText()
            throw RequestException(message)
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

        if (response.status != HttpStatusCode.OK) {
            checkUnauthorizedStatus(response)

            val message = response.bodyAsText()
            throw RequestException(message)
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

        if (response.status != HttpStatusCode.OK) {
            checkUnauthorizedStatus(response)

            val message = response.bodyAsText()
            throw RequestException(message)
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

        if (response.status != HttpStatusCode.OK) {
            checkUnauthorizedStatus(response)

            val message = response.bodyAsText()
            throw RequestException(message)
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

        if (response.status != HttpStatusCode.OK) {
            checkUnauthorizedStatus(response)

            val message = response.bodyAsText()
            throw RequestException(message)
        }

        val jsonItem = response.body<String>()
        val todoItem = Json
            .decodeFromString<TodoItem>(jsonItem)

        return todoItem
    }

    private fun checkUnauthorizedStatus(response: HttpResponse) {
        if (response.status == HttpStatusCode.Unauthorized) {
            token = ""
        }
    }

    fun tokenHasExpired() = !token.isNullOrEmpty()

    fun clearToken() {
        token = ""
    }
}