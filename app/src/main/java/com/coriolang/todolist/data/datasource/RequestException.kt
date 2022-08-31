package com.coriolang.todolist.data.datasource

import io.ktor.client.statement.*

class RequestException(response: HttpResponse) : Throwable() {

    override val message = "${response.status.value} ${response.status.description}"
}