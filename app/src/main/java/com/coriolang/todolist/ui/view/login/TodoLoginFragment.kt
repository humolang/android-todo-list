package com.coriolang.todolist.ui.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.databinding.FragmentTodoLoginBinding
import com.coriolang.todolist.ioc.login.TodoLoginFragmentComponent
import com.coriolang.todolist.ioc.login.TodoLoginFragmentViewComponent
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoLoginFragment : Fragment(R.layout.fragment_todo_login) {

    private val applicationComponent
        get() = TodoApplication.get(requireContext()).applicationComponent
    private lateinit var fragmentComponent: TodoLoginFragmentComponent
    private var fragmentViewComponent: TodoLoginFragmentViewComponent? = null

    private var _binding: FragmentTodoLoginBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels {
        applicationComponent.viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent = TodoLoginFragmentComponent(
            applicationComponent = applicationComponent,
            fragment = this,
            viewModel = viewModel
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoLoginBinding
            .inflate(inflater, container, false)

        fragmentViewComponent = TodoLoginFragmentViewComponent(
            fragmentComponent = fragmentComponent,
            binding = binding,
            lifecycleOwner = viewLifecycleOwner
        ).apply { todoLoginViewController.setupViews() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentViewComponent = null
        _binding = null
    }
}