package com.coriolang.todolist.ui.view.list

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.todolist.R
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.ui.OK
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import kotlinx.coroutines.launch

class TodoListViewController(
    private val activity: FragmentActivity,
    private val fragment: Fragment,
    private val binding: FragmentTodoListBinding,
    private val adapter: TodoListAdapter,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: TodoListViewModel
    ) {

    fun setupViews() {
        setupMenu()
        setupSwipeRefresh()
        setupRecyclerView()
        setupFab()
        setupExceptionToast()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = activity

        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.option_login -> navigateToLogin()
                }

                return true
            }
        }, lifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshTodo.setOnRefreshListener {
            viewModel.refreshTodoList()
            binding.swipeRefreshTodo.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewTodo
            .layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewTodo.adapter = adapter

        fragment.lifecycleScope.launch {
            observeTodoItems()
        }
    }

    private fun setupFab() {
        binding.fabAddItem.setOnClickListener {
            navigateToItem()
        }
    }

    private fun setupExceptionToast() {
        fragment.lifecycleScope.launch {
            viewModel.exceptionMessage.collect {
                if (it.isNotEmpty() && it != OK) {
                    showToast(it)
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            fragment.context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private suspend fun observeTodoItems() {
        viewModel.todoItems.collect {
            adapter.submitList(it)
        }
    }

    private fun navigateToLogin() {
        val action = TodoListFragmentDirections
            .actionTodoListFragmentToTodoLoginFragment()
        fragment.findNavController().navigate(action)
    }

    private fun navigateToItem() {
        val action = TodoListFragmentDirections
            .actionTodoListFragmentToTodoEditFragment()
        fragment.findNavController().navigate(action)
    }
}