package com.coriolang.todolist.data.user

import kotlinx.serialization.Serializable

@Serializable
data class User(

    val username: String,
    val password: String
)
