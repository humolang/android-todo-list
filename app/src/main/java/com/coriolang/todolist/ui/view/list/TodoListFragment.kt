package com.coriolang.todolist.ui.view.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.ioc.list.TodoListFragmentComponent
import com.coriolang.todolist.ioc.list.TodoListFragmentViewComponent
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private val applicationComponent
        get() = TodoApplication.get(requireContext()).applicationComponent
    private lateinit var fragmentComponent: TodoListFragmentComponent
    private var fragmentViewComponent: TodoListFragmentViewComponent? = null

    private var _binding: FragmentTodoListBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels {
        applicationComponent.viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fragmentComponent = TodoListFragmentComponent(
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
        _binding = FragmentTodoListBinding
            .inflate(inflater, container, false)

        fragmentViewComponent = TodoListFragmentViewComponent(
            fragmentComponent = fragmentComponent,
            binding = binding,
            lifecycleOwner = viewLifecycleOwner
        ).apply { todoListViewController.setupViews() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentViewComponent = null
        _binding = null
    }
}