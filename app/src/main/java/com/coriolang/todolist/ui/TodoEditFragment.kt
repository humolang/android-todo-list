package com.coriolang.todolist.ui

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.data.todoItem.Importance
import com.coriolang.todolist.databinding.FragmentTodoEditBinding
import com.coriolang.todolist.viewmodels.TodoListViewModel
import com.coriolang.todolist.viewmodels.TodoListViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch

class TodoEditFragment : Fragment(R.layout.fragment_todo_edit) {

    private var _binding: FragmentTodoEditBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels {
        TodoListViewModelFactory(
            (activity?.application as TodoApplication).database.todoItemDao()
        )
    }

    private var todoItemId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            todoItemId = it.getInt("todoItemId")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.todoItem.collect {
                if (it.deadlineDate > 0L) {
                    binding.switchDeadlineDate.isChecked = true

                    binding.buttonDeadlineDate.isEnabled = true
                    binding.buttonDeadlineDate.text = DateUtils
                        .formatDateTime(context, it.deadlineDate, DateUtils.FORMAT_SHOW_DATE)
                } else {
                    binding.switchDeadlineDate.isChecked = false

                    binding.buttonDeadlineDate.isEnabled = false
                    binding.buttonDeadlineDate.text = getString(R.string.select_date)
                }

                binding.buttonImportanceMenu.text = when (it.importance) {
                    Importance.LOW -> getString(R.string.low_importance)
                    Importance.NORMAL -> getString(R.string.normal_importance)
                    Importance.HIGH -> getString(R.string.high_importance)
                }
            }
        }

        if (todoItemId > 0) {
            viewModel.findTodoItemById(todoItemId)

            binding.textFieldTodo.editText
                ?.setText(viewModel.todoItem.value.text)
        } else {
            viewModel.defaultTodoItem()
        }

        if (todoItemId > 0) {
            binding.buttonDelete.setOnClickListener {
                viewModel.deleteTodoItem()

                val action = TodoEditFragmentDirections
                    .actionTodoEditFragmentToTodoListFragment()
                findNavController().navigate(action)
            }
        } else {
            binding.buttonDelete.visibility = View.GONE
        }

        binding.buttonSave.setOnClickListener {
            val text = binding.textFieldTodo.editText?.text.toString()
            viewModel.setTodoText(text)

            if (todoItemId > 0) {
                viewModel.updateTodoItem()
            } else {
                viewModel.insertTodoItem()
            }

            val action = TodoEditFragmentDirections
                .actionTodoEditFragmentToTodoListFragment()
            findNavController().navigate(action)
        }

        binding.switchDeadlineDate.setOnCheckedChangeListener { _, isChecked ->
            binding.buttonDeadlineDate.isEnabled = isChecked
            if (!isChecked) {
                viewModel.setTodoDeadlineDate(0L)
            }
        }

        val deadlineDatePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.select_deadline_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        deadlineDatePicker.addOnPositiveButtonClickListener { selectedDate ->
            viewModel.setTodoDeadlineDate(selectedDate)
        }

        binding.buttonDeadlineDate.setOnClickListener {
            deadlineDatePicker.show(childFragmentManager, "tag")
        }

        binding.buttonImportanceMenu.setOnClickListener {
            val importanceMenu = PopupMenu(requireContext(), it)

            importanceMenu.menuInflater.inflate(R.menu.importance_menu, importanceMenu.menu)
            importanceMenu.setForceShowIcon(true)

            importanceMenu.setOnMenuItemClickListener { menuItem ->
                val importance = when (menuItem.itemId) {
                    R.id.low_importance_option -> Importance.LOW
                    R.id.normal_importance_option -> Importance.NORMAL
                    else -> Importance.HIGH
                }
                viewModel.setTodoImportance(importance)

                true
            }

            importanceMenu.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
