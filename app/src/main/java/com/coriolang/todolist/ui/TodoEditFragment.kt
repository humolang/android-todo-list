package com.coriolang.todolist.ui

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.R
import com.coriolang.todolist.TodoApplication
import com.coriolang.todolist.databinding.FragmentTodoEditBinding
import com.coriolang.todolist.viewmodels.TodoListViewModel
import com.coriolang.todolist.viewmodels.TodoListViewModelFactory
import com.google.android.material.datepicker.MaterialDatePicker

class TodoEditFragment : Fragment(R.layout.fragment_todo_edit) {

    private var _binding: FragmentTodoEditBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels {
        TodoListViewModelFactory(
            (activity?.application as TodoApplication).database.todoItemDao()
        )
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

        binding.buttonDeadlineDate.isEnabled = binding.switchDeadlineDate.isChecked

        binding.switchDeadlineDate.setOnCheckedChangeListener { _, isChecked ->
            binding.buttonDeadlineDate.isEnabled = isChecked
        }

        val deadlineDatePicker = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(getString(R.string.select_deadline_date))
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

        deadlineDatePicker.addOnPositiveButtonClickListener { selectedDate ->
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
                binding.buttonImportanceMenu.text = menuItem.title
                true
            }

            importanceMenu.show()
        }

        binding.buttonSave.setOnClickListener {
            val action = TodoEditFragmentDirections
                .actionTodoEditFragmentToTodoListFragment()
            findNavController().navigate(action)
        }

        binding.buttonDelete.setOnClickListener {
            val action = TodoEditFragmentDirections
                .actionTodoEditFragmentToTodoListFragment()
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
