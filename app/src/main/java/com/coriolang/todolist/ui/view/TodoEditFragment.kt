package com.coriolang.todolist.ui.view

import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.OK
import com.coriolang.todolist.R
import com.coriolang.todolist.data.model.Importance
import com.coriolang.todolist.databinding.FragmentTodoEditBinding
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoEditFragment : Fragment(R.layout.fragment_todo_edit) {

    private var id: String = ""
    companion object {
        private const val ID_KEY = "id"
    }

    private var _binding: FragmentTodoEditBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: TodoListViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            id = it.getString(ID_KEY, "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoEditBinding
            .inflate(inflater, container, false)

        setupViews()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupViews() {
        setupTextField()
        setupSwitchDeadline()
        setupDeadlinePicker()
        setupButtonImportance()
        setupButtonSave()
        setupButtonDelete()
        setupExceptionToast()

        viewModel.findTodoItemById(id)

        setTextFieldContent(viewModel.todoText.value)
        setDeadlineViewContent(viewModel.todoDeadline.value)
        setImportanceViewContent(viewModel.todoImportance.value)

        viewLifecycleOwner.lifecycleScope.launch {
            launch { observeDeadlineDate() }
            launch { observeImportance() }
        }
    }

    private fun setupTextField() {
        binding.textFieldTodo.editText?.addTextChangedListener {
            viewModel.setTodoText(it.toString())
        }
    }

    private fun setupSwitchDeadline() {
        binding.switchDeadlineDate.setOnCheckedChangeListener { _, isChecked ->
            binding.buttonDeadlineDate.isEnabled = isChecked
        }
    }

    private fun setupDeadlinePicker() {
        val deadlineDatePicker = buildDatePicker()

        deadlineDatePicker.addOnPositiveButtonClickListener { selectedDate ->
            viewModel.setTodoDeadline(selectedDate)
        }

        binding.buttonDeadlineDate.setOnClickListener {
            deadlineDatePicker.show(childFragmentManager, "tag")
        }
    }

    private fun setupButtonImportance() {
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

    private fun setupButtonSave() {
        binding.buttonSave.setOnClickListener {
            if (!binding.switchDeadlineDate.isChecked) {
                viewModel.setTodoDeadline(0L)
            }

            if (id.isEmpty()) {
                viewModel.insertTodoItem()
            } else {
                viewModel.updateTodoItem(id)
            }

            navigateToList()
        }
    }

    private fun setupButtonDelete() {
        if (id.isEmpty()) {
            binding.buttonDelete.visibility = View.GONE
        } else {
            binding.buttonDelete.setOnClickListener {
                viewModel.deleteTodoItem(id)
                navigateToList()
            }
        }
    }

    private fun setupExceptionToast() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exceptionMessage.collect {
                if (it.isNotEmpty() && it != OK) {
                    showToast(it)
                }
            }
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(
            context,
            text,
            Toast.LENGTH_LONG
        ).show()
    }

    private fun setTextFieldContent(text: String) {
        binding.textFieldTodo.editText?.setText(text)
    }

    private fun setDeadlineViewContent(deadline: Long) {
        val hasDeadline = deadline > 0L

        binding.switchDeadlineDate.isChecked = hasDeadline
        binding.buttonDeadlineDate.isEnabled = hasDeadline

        if (hasDeadline) {
            binding.buttonDeadlineDate.text = DateUtils
                .formatDateTime(context, deadline, DateUtils.FORMAT_SHOW_DATE)
        } else {
            binding.buttonDeadlineDate.text = getString(R.string.select_date)
        }
    }

    private fun setImportanceViewContent(importance: Importance) {
        binding.buttonImportanceMenu.text = when (importance) {
            Importance.LOW -> getString(R.string.low_importance)
            Importance.NORMAL -> getString(R.string.normal_importance)
            Importance.HIGH -> getString(R.string.high_importance)
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(
                getString(R.string.select_deadline_date)
            )
            .setSelection(
                MaterialDatePicker.todayInUtcMilliseconds()
            )
            .build()
    }

    private fun navigateToList() {
        val action = TodoEditFragmentDirections
            .actionTodoEditFragmentToTodoListFragment()
        findNavController().navigate(action)
    }

    private suspend fun observeDeadlineDate() {
        viewModel.todoDeadline.collect {
            setDeadlineViewContent(it)
        }
    }

    private suspend fun observeImportance() {
        viewModel.todoImportance.collect {
            setImportanceViewContent(it)
        }
    }
}
