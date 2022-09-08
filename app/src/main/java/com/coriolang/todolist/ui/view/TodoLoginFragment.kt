package com.coriolang.todolist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.OK
import com.coriolang.todolist.R
import com.coriolang.todolist.databinding.FragmentTodoLoginBinding
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoLoginFragment : Fragment(R.layout.fragment_todo_login) {

    private var _binding: FragmentTodoLoginBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoLoginBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
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
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exceptionMessage.collect {
                if (it == OK) {
                    navigateToList()
                } else if (it.isNotEmpty()) {
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

    private fun getUsername() = binding.textInputUsername
        .editText?.text.toString()

    private fun getPassword() = binding.textInputPassword
        .editText?.text.toString()

    private fun navigateToList() {
        val action = TodoLoginFragmentDirections
            .actionTodoLoginFragmentToTodoListFragment()
        findNavController().navigate(action)
    }
}