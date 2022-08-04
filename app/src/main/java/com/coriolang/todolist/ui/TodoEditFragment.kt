package com.coriolang.todolist.ui

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
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

    private var deadlineDate = 0L
    private var importance: Importance = Importance.NORMAL

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

        if (todoItemId > 0) {
            lifecycleScope.launch {
                val todoItem = viewModel.findTodoItemById(todoItemId)

                binding.apply {
                    textFieldTodo.editText?.setText(todoItem.text)

                    if (todoItem.deadlineDate > 0L) {
                        switchDeadlineDate.isChecked = true
                        deadlineDate = todoItem.deadlineDate
                        buttonDeadlineDate.text = DateUtils
                            .formatDateTime(context, deadlineDate, DateUtils.FORMAT_SHOW_DATE)
                    }

                    importance = todoItem.importance
                    buttonImportanceMenu.text = when (importance) {
                        Importance.LOW -> getString(R.string.low_importance)
                        Importance.NORMAL -> getString(R.string.normal_importance)
                        Importance.HIGH -> getString(R.string.high_importance)
                    }

                    buttonDelete.visibility = View.VISIBLE
                    buttonDelete.setOnClickListener {
                        viewModel.deleteTodoItem(todoItem)

                        val action = TodoEditFragmentDirections
                            .actionTodoEditFragmentToTodoListFragment()
                        findNavController().navigate(action)
                    }

                    buttonSave.setOnClickListener {
                        val text = binding.textFieldTodo.editText?.text.toString()

                        val updatedTodoItem = todoItem.copy(
                            text = text,
                            importance = importance,
                            deadlineDate = deadlineDate,
                            modificationDate = System.currentTimeMillis()
                        )
                        viewModel.updateTodoItem(updatedTodoItem)

                        val action = TodoEditFragmentDirections
                            .actionTodoEditFragmentToTodoListFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        } else {
            binding.buttonSave.setOnClickListener {
                val text = binding.textFieldTodo.editText?.text.toString()

                if (binding.switchDeadlineDate.isChecked) {
                    viewModel.insertTodoItem(text, importance, deadlineDate)
                } else {
                    viewModel.insertTodoItem(text, importance)
                }

                val action = TodoEditFragmentDirections
                    .actionTodoEditFragmentToTodoListFragment()
                findNavController().navigate(action)
            }
        }

        binding.buttonDeadlineDate.isEnabled = binding.switchDeadlineDate.isChecked

        binding.switchDeadlineDate.setOnCheckedChangeListener { _, isChecked ->
            binding.buttonDeadlineDate.isEnabled = isChecked
            if (!isChecked) {
                deadlineDate = 0L
            }
        }

        val deadlineDatePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.select_deadline_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        deadlineDatePicker.addOnPositiveButtonClickListener { selectedDate ->
            deadlineDate = selectedDate
            binding.buttonDeadlineDate.text = DateUtils
                .formatDateTime(context, selectedDate, DateUtils.FORMAT_SHOW_DATE)
        }

        binding.buttonDeadlineDate.setOnClickListener {
            deadlineDatePicker.show(childFragmentManager, "tag")
        }

        binding.buttonImportanceMenu.setOnClickListener {
            val importanceMenu = PopupMenu(requireContext(), it)

            importanceMenu.menuInflater.inflate(R.menu.importance_menu, importanceMenu.menu)
            importanceMenu.setForceShowIcon(true)

            importanceMenu.setOnMenuItemClickListener { menuItem ->
                importance = when (menuItem.itemId) {
                    R.id.low_importance_option -> Importance.LOW
                    R.id.normal_importance_option -> Importance.NORMAL
                    else -> Importance.HIGH
                }
                binding.buttonImportanceMenu.text = menuItem.title
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
