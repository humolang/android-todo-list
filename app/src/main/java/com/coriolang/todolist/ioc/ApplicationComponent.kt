package com.coriolang.todolist.ioc

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.data.datasource.TodoApi
import com.coriolang.todolist.data.datasource.TodoDatabase
import com.coriolang.todolist.data.repository.TodoItemRepository

class ApplicationComponent(context: Context) {

    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val sharedPreferences = EncryptedSharedPreferences.create(
        "encrypted_shared_prefs",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val database = TodoDatabase.getDatabase(TodoApplication.get(context))
    private val api = TodoApi(sharedPreferences)

    private val repository = TodoItemRepository(
        database.todoItemDao(),
        api
    )

    val viewModelFactory = TodoListViewModelFactory(repository)
}