package com.coriolang.todolist.ioc.login

import androidx.fragment.app.Fragment
import com.coriolang.todolist.ioc.ApplicationComponent
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoLoginFragmentComponent(
    val applicationComponent: ApplicationComponent,
    val fragment: Fragment,
    val viewModel: TodoListViewModel
    )