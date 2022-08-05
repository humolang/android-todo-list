package com.coriolang.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.viewmodels.TodoListViewModel
import com.coriolang.todolist.viewmodels.TodoListViewModelFactory
import kotlinx.coroutines.launch

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private var _binding: FragmentTodoListBinding? = null
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
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onCheckboxClicked = { id: Int, isCompleted: Boolean ->
            viewModel.setTodoIsCompleted(id, isCompleted)
        }
        val onTodoItemClicked = { id: Int ->
            val action = TodoListFragmentDirections
                .actionTodoListFragmentToTodoEditFragment(id)
            findNavController().navigate(action)
        }

        val recyclerView = binding.recyclerViewTodo
        val adapter = TodoListAdapter(onCheckboxClicked, onTodoItemClicked)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.allTodoItems().collect {
                adapter.submitList(it)
            }
        }

        binding.fabAddItem.setOnClickListener {
            val action = TodoListFragmentDirections
                .actionTodoListFragmentToTodoEditFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}