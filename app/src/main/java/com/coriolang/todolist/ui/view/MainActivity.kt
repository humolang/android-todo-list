package com.coriolang.todolist.ui.view

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.coriolang.todolist.R
import com.coriolang.todolist.callbacks.NetworkCallback
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewModel: TodoListViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupNavController()
        setupConnectivityManager()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    private fun setupNavController() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    private fun setupConnectivityManager() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = NetworkCallback(viewModel)
        val connectivityManager = getSystemService(
            ConnectivityManager::class.java
        ) as ConnectivityManager

        connectivityManager
            .registerNetworkCallback(
                networkRequest,
                networkCallback
            )
    }
}