package com.coriolang.todolist.ui.view.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.databinding.FragmentTodoEditBinding
import com.coriolang.todolist.ioc.edit.TodoEditFragmentComponent
import com.coriolang.todolist.ioc.edit.TodoEditFragmentViewComponent
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoEditFragment : Fragment(R.layout.fragment_todo_edit) {

    private val applicationComponent
        get() = TodoApplication.get(requireContext()).applicationComponent
    private lateinit var fragmentComponent: TodoEditFragmentComponent
    private var fragmentViewComponent: TodoEditFragmentViewComponent? = null

    private var id: String = ""
    companion object {
        private const val ID = "id"
    }

    private var _binding: FragmentTodoEditBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels {
        applicationComponent.viewModelFactory
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            id = it.getString(ID, "")
        }

        fragmentComponent = TodoEditFragmentComponent(
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
        _binding = FragmentTodoEditBinding
            .inflate(inflater, container, false)

        fragmentViewComponent = TodoEditFragmentViewComponent(
            fragmentComponent = fragmentComponent,
            binding = binding,
            lifecycleOwner = viewLifecycleOwner,
            id = id
        ).apply { todoEditViewController.setupViews() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        fragmentViewComponent = null
        _binding = null
    }
}
