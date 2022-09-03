package com.coriolang.todolist.exceptions

import io.ktor.client.statement.*

class RequestException(message: String) : Throwable(message) {

    override val message: String
        get() = super.message ?: ""

    constructor(response: HttpResponse)
        : this("${response.status.value} ${response.status.description}")
}