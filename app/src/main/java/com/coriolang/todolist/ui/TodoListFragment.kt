package com.coriolang.todolist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.todolist.R
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.model.TodoItemsRepository

class TodoListFragment : Fragment(R.layout.fragment_todo_list) {

    private var _binding: FragmentTodoListBinding? = null
    private val binding
        get() = _binding!!

    private val repository = TodoItemsRepository()

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

        binding.recyclerViewTodo
            .layoutManager = LinearLayoutManager(context)
        binding.recyclerViewTodo.adapter = TodoItemAdapter(repository.todoItems)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}