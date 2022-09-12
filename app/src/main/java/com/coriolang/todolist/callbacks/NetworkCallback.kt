package com.coriolang.todolist.callbacks

import android.net.ConnectivityManager
import android.net.Network
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class NetworkCallback(
    private val viewModel: TodoListViewModel
    ) : ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)

        viewModel.refreshTodoList()
    }
}