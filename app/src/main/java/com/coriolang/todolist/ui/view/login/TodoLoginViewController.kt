package com.coriolang.todolist.ui.view.login

import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.databinding.FragmentTodoLoginBinding
import com.coriolang.todolist.ui.NAVIGATE_TO_LIST
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import kotlinx.coroutines.launch

class TodoLoginViewController(
    private val activity: FragmentActivity,
    private val fragment: Fragment,
    private val binding: FragmentTodoLoginBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: TodoListViewModel
    ) {

    fun setupViews() {
        setupButtonRegistration()
        setupButtonLogin()
        setupButtonOffline()
        setupExceptionToast()
    }

    private fun setupButtonRegistration() {
        binding.buttonRegistration.setOnClickListener {
            val username = getUsername()
            val password = getPassword()

            viewModel.registerUser(username, password)
        }
    }

    private fun setupButtonLogin() {
        binding.buttonLogin.setOnClickListener {
            val username = getUsername()
            val password = getPassword()

            viewModel.loginUser(username, password)
        }
    }

    private fun setupButtonOffline() {
        binding.buttonOffline.setOnClickListener {
            navigateToList()
        }
    }

    private fun setupExceptionToast() {
        fragment.lifecycleScope.launch {
            viewModel.exceptionMessage.collect {
                if (it == NAVIGATE_TO_LIST) {
                    navigateToList()
                } else if (it.isNotEmpty()) {
                    Toast.makeText(
                        fragment.context,
                        it,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getUsername() = binding.textInputUsername
        .editText?.text.toString()

    private fun getPassword() = binding.textInputPassword
        .editText?.text.toString()

    private fun navigateToList() {
        val action = TodoLoginFragmentDirections
            .actionTodoLoginFragmentToTodoListFragment()
        fragment.findNavController().navigate(action)
    }
}