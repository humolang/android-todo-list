package com.coriolang.todolist.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.coriolang.todolist.R
import com.coriolang.todolist.databinding.FragmentTodoListBinding
import com.coriolang.todolist.data.Importance
import com.coriolang.todolist.data.TodoItem
import com.coriolang.todolist.data.TodoItemsRepository
import java.util.*

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

        val recyclerView = binding.recyclerViewTodo
        val adapter = TodoListAdapter()

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        adapter.submitList(repository.todoItems)

        binding.buttonAddItem.setOnClickListener {
            val id = (repository.todoItems.size + 1).toString()
            val text = "text$id"
            val importance = Importance.LOW
            val idCompleted = true

            val todoItem = TodoItem(
                id,
                text,
                importance,
                0L,
                idCompleted,
                Date().time,
                0L
            )

            repository.addItem(todoItem)
            adapter.notifyItemInserted(repository.todoItems.size - 1)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}