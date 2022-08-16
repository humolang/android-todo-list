package com.coriolang.todolist.ui.view

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import com.coriolang.todolist.ui.viewmodels.TodoListViewModelFactory
import kotlinx.coroutines.launch

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private var _binding: FragmentTodoListBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels {
        TodoListViewModelFactory(
            (activity?.application as TodoApplication).repository
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

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(
                menu: Menu,
                menuInflater: MenuInflater
            ) {
                menuInflater.inflate(R.menu.options_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.option_login -> {
                        val action = TodoListFragmentDirections
                            .actionTodoListFragmentToTodoLoginFragment()
                        findNavController().navigate(action)
                    }
                }

                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.swipeRefreshTodo.setOnRefreshListener {
        }

        val onCheckboxClicked = { id: String, isCompleted: Boolean ->
            viewModel.setTodoIsCompleted(id, isCompleted)
        }
        val onTodoItemClicked = { id: String ->
            val action = TodoListFragmentDirections
                .actionTodoListFragmentToTodoEditFragment(id = id)
            findNavController().navigate(action)
        }

        val recyclerView = binding.recyclerViewTodo
        val adapter = TodoListAdapter(onCheckboxClicked, onTodoItemClicked)

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            viewModel.todoItems.collect {
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