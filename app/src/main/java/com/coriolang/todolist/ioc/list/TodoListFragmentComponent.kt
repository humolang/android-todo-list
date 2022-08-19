package com.coriolang.todolist.ioc.list

import androidx.fragment.app.Fragment
import com.coriolang.todolist.ioc.ApplicationComponent
import com.coriolang.todolist.ui.view.list.TodoListAdapter
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel

class TodoListFragmentComponent(
    val applicationComponent: ApplicationComponent,
    val fragment: Fragment,
    val viewModel: TodoListViewModel
    ) {

    val adapter = TodoListAdapter(viewModel)
}