package com.coriolang.todolist.data

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

class TodoNetwork {

    companion object {
        const val BASE_URL = "https://10.0.2.2:8080"
    }

    private val client = HttpClient(OkHttp)
    private var token = ""

    suspend fun registrationRequest(user: User) {
        val jsonUser = Json.encodeToString(user)

        withContext(Dispatchers.IO) {
            client.post("$BASE_URL/reg") {
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
}