package com.coriolang.todolist.ui.view.login

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.databinding.FragmentTodoLoginBinding

class TodoLoginViewController(
    private val activity: FragmentActivity,
    private val fragment: Fragment,
    private val binding: FragmentTodoLoginBinding,
    private val lifecycleOwner: LifecycleOwner
    ) {

    fun setupViews() {
        setupButtonRegistration()
        setupButtonLogin()
        setupButtonOffline()
    }

    private fun setupButtonRegistration() {
        binding.buttonRegistration.setOnClickListener {
            val username = getUsername()
            val password = getPassword()

            navigateToList()
        }
    }

    private fun setupButtonLogin() {
        binding.buttonLogin.setOnClickListener {
            val username = getUsername()
            val password = getPassword()

            navigateToList()
        }
    }

    private fun setupButtonOffline() {
        binding.buttonOffline.setOnClickListener {
            navigateToList()
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