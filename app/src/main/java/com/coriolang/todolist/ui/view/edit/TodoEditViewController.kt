package com.coriolang.todolist.ui.view.edit

import android.text.format.DateUtils
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.coriolang.todolist.R
import com.coriolang.todolist.data.model.Importance
import com.coriolang.todolist.databinding.FragmentTodoEditBinding
import com.coriolang.todolist.ui.viewmodels.TodoListViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch

class TodoEditViewController(
    private val activity: FragmentActivity,
    private val fragment: Fragment,
    private val binding: FragmentTodoEditBinding,
    private val lifecycleOwner: LifecycleOwner,
    private val viewModel: TodoListViewModel,
    private val id: String
    ) {

    fun setupViews() {
        setupTextField()
        setupSwitchDeadline()
        setupDeadlinePicker()
        setupButtonImportance()
        setupButtonSave()
        setupButtonDelete()

        setTextFieldContent(viewModel.todoText.value)
        setDeadlineViewContent(viewModel.todoDeadline.value)
        setImportanceViewContent(viewModel.todoImportance.value)

        activity.lifecycleScope.launch {
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
            deadlineDatePicker.show(fragment.childFragmentManager, "tag")
        }
    }

    private fun setupButtonImportance() {
        binding.buttonImportanceMenu.setOnClickListener {
            val importanceMenu = PopupMenu(activity, it)

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

    private fun setTextFieldContent(text: String) {
        binding.textFieldTodo.editText?.setText(text)
    }

    private fun setDeadlineViewContent(deadline: Long) {
        val hasDeadline = deadline > 0L

        binding.switchDeadlineDate.isChecked = hasDeadline
        binding.buttonDeadlineDate.isEnabled = hasDeadline

        if (hasDeadline) {
            binding.buttonDeadlineDate.text = DateUtils
                .formatDateTime(activity, deadline, DateUtils.FORMAT_SHOW_DATE)
        } else {
            binding.buttonDeadlineDate.text = activity.getString(R.string.select_date)
        }
    }

    private fun setImportanceViewContent(importance: Importance) {
        binding.buttonImportanceMenu.text = when (importance) {
            Importance.LOW -> activity.getString(R.string.low_importance)
            Importance.NORMAL -> activity.getString(R.string.normal_importance)
            Importance.HIGH -> activity.getString(R.string.high_importance)
        }
    }

    private fun buildDatePicker(): MaterialDatePicker<Long> {
        return MaterialDatePicker.Builder
            .datePicker()
            .setTitleText(
                activity.getString(R.string.select_deadline_date)
            )
            .setSelection(
                MaterialDatePicker.todayInUtcMilliseconds()
            )
            .build()
    }

    private fun navigateToList() {
        val action = TodoEditFragmentDirections
            .actionTodoEditFragmentToTodoListFragment()
        fragment.findNavController().navigate(action)
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