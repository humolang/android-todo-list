package com.coriolang.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.databinding.FragmentTodoLoginBinding
import com.coriolang.todolist.viewmodels.TodoListViewModel
import com.coriolang.todolist.viewmodels.TodoListViewModelFactory

class TodoLoginFragment : Fragment(R.layout.fragment_todo_login) {

    private var _binding: FragmentTodoLoginBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels {
        TodoListViewModelFactory(
            (activity?.application as TodoApplication).database.todoItemDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoLoginBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonRegistration.setOnClickListener {
            val username = binding.textInputUsername.editText
                ?.text.toString()
            val password = binding.textInputPassword.editText
                ?.text.toString()

            navigateToList()
        }

        binding.buttonLogin.setOnClickListener {
            val username = binding.textInputUsername.editText
                ?.text.toString()
            val password = binding.textInputPassword.editText
                ?.text.toString()

            navigateToList()
        }
        
        binding.buttonOffline.setOnClickListener {
            navigateToList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToList() {
        val action = TodoLoginFragmentDirections
            .actionTodoLoginFragmentToTodoListFragment()
        findNavController().navigate(action)
    }
}