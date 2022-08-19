package com.coriolang.todolist.ioc.edit

import androidx.lifecycle.LifecycleOwner
import com.coriolang.todolist.databinding.FragmentTodoEditBinding
import com.coriolang.todolist.ui.view.edit.TodoEditViewController

class TodoEditFragmentViewComponent(
    fragmentComponent: TodoEditFragmentComponent,
    binding: FragmentTodoEditBinding,
    lifecycleOwner: LifecycleOwner,
    id: String
) {

    val todoEditViewController = TodoEditViewController(
        activity = fragmentComponent.fragment.requireActivity(),
        fragment = fragmentComponent.fragment,
        binding = binding,
        lifecycleOwner = lifecycleOwner,
        viewModel = fragmentComponent.viewModel,
        id = id
    )
}