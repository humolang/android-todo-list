package com.coriolang.todolist.ioc.edit

import androidx.fragment.app.Fragment
import com.coriolang.todolist.ioc.ApplicationComponent
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoEditFragmentComponent(
    val applicationComponent: ApplicationComponent,
    val fragment: Fragment,
    val viewModel: TodoListViewModel
    )