package com.coriolang.todolist.ioc.list

import androidx.lifecycle.LifecycleOwner
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.ui.view.list.TodoListViewController

class TodoListFragmentViewComponent(
    fragmentComponent: TodoListFragmentComponent,
    binding: FragmentTodoListBinding,
    lifecycleOwner: LifecycleOwner
) {

    val todoListViewController = TodoListViewController(
        activity = fragmentComponent.fragment.requireActivity(),
        fragment = fragmentComponent.fragment,
        binding = binding,
        adapter = fragmentComponent.adapter,
        lifecycleOwner = lifecycleOwner,
        viewModel = fragmentComponent.viewModel
    )
}