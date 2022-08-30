package com.coriolang.todolist.ioc.login

import androidx.lifecycle.LifecycleOwner
import com.coriolang.todolist.databinding.FragmentTodoLoginBinding
import com.coriolang.todolist.ui.view.login.TodoLoginViewController

class TodoLoginFragmentViewComponent(
    fragmentComponent: TodoLoginFragmentComponent,
    binding: FragmentTodoLoginBinding,
    lifecycleOwner: LifecycleOwner
) {

    val todoLoginViewController = TodoLoginViewController(
        activity = fragmentComponent.fragment.requireActivity(),
        fragment = fragmentComponent.fragment,
        binding = binding,
        lifecycleOwner = lifecycleOwner,
        viewModel = fragmentComponent.viewModel
    )
}