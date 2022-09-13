package com.coriolang.todolist.ui.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.todolist.OK
import com.coriolang.todolist.R
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private var _binding: FragmentTodoListBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoListBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        setupMenu()
        setupSwipeRefresh()
        setupRecyclerView()
        setupFab()
        setupExceptionToast()
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.options_menu, menu)

                val usernameIsEmpty = viewModel.usernameIsEmpty()
                val tokenHasExpired = viewModel.tokenHasExpired()

                val loginItem = menu.findItem(R.id.option_login)
                val logoutItem = menu.findItem(R.id.option_logout)

                loginItem.isVisible = usernameIsEmpty && tokenHasExpired
                logoutItem.isVisible = !usernameIsEmpty && !tokenHasExpired
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.option_login -> onLoginClicked()
                    R.id.option_logout -> onLogoutClicked()
                }

                return true
            }

            private fun onLoginClicked() {
                navigateToLogin()
            }

            private fun onLogoutClicked() {
                viewModel.clearUsername()
                viewModel.clearToken()

                menuHost.invalidateMenu()
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshTodo.setOnRefreshListener {
            if (!viewModel.usernameIsEmpty()
                && !viewModel.tokenHasExpired()) {

                viewModel.refreshTodoList()
            }

            binding.swipeRefreshTodo.isRefreshing = false
        }
    }

    private fun setupRecyclerView() {
        val adapter = TodoListAdapter(viewModel)

        binding.recyclerViewTodo
            .layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewTodo.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            observeTodoItems(adapter)
        }
    }

    private fun setupFab() {
        binding.fabAddItem.setOnClickListener {
            navigateToItem()
        }
    }

    private fun setupExceptionToast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exceptionMessage.collect {
                if (it.isNotEmpty() && it != OK) {
                    showToast(it)
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private suspend fun observeTodoItems(adapter: TodoListAdapter) {
        viewModel.todoItems.collect {
            adapter.submitList(it)
        }
    }

    private fun navigateToLogin() {
        val action = TodoListFragmentDirections
            .actionTodoListFragmentToTodoLoginFragment()
        findNavController().navigate(action)
    }

    private fun navigateToItem() {
        val action = TodoListFragmentDirections
            .actionTodoListFragmentToTodoEditFragment()
        findNavController().navigate(action)
    }
}