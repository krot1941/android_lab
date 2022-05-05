package com.example.loversdiary.ui.addeditmoment

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.loversdiary.R
import com.example.loversdiary.databinding.AddEditMomentFragmentBinding
import com.example.loversdiary.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class AddEditMomentFragment : Fragment(R.layout.add_edit_moment_fragment) {

    private val addEditMomentViewModel: AddEditMomentViewModel by viewModels()

    private val calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = AddEditMomentFragmentBinding.bind(view)

        binding.apply {

            val spinnerAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    addEditMomentViewModel.stringEventList
            )
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            addEditMomentSpinner.apply {
                adapter = spinnerAdapter
                setSelection(addEditMomentViewModel.eventsList.indexOf(addEditMomentViewModel.momentEvent))
            }

            addEditMomentPlaceView.setText(addEditMomentViewModel.momentPlace)
            addEditMomentNoteView.setText(addEditMomentViewModel.momentNote)
            addEditMomentDateView.text = addEditMomentViewModel.momentDate

            addEditMomentPlaceView.addTextChangedListener {
                addEditMomentViewModel.momentPlace = it.toString()
            }
            addEditMomentNoteView.addTextChangedListener {
                addEditMomentViewModel.momentNote = it.toString()
            }

            addEditMomentDateView.setOnClickListener {
                DatePickerDialog(requireContext(), dateSetListener,
                        addEditMomentViewModel.dateYear,
                        addEditMomentViewModel.dateMonth,
                        addEditMomentViewModel.dateDay).show()
            }

            addEditMomentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, itemSelected: View?, selectedItemPosition: Int, selectedId: Long) {
                    if(itemSelected != null) {
                        addEditMomentViewModel.momentEvent = addEditMomentViewModel.eventsList[selectedItemPosition]
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

            addEditMomentFABAddView.setOnClickListener {
                addEditMomentViewModel.onSaveMomentClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            addEditMomentViewModel.addEditMomentEvent.collect { event ->
                when (event) {
                    is AddEditMomentViewModel.AddEditMomentEvent.ChangeDateView -> {
                        binding.addEditMomentDateView.text = addEditMomentViewModel.momentDate
                    }
                    is AddEditMomentViewModel.AddEditMomentEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is AddEditMomentViewModel.AddEditMomentEvent.NavigateBackWithResult -> {
                        setFragmentResult("add_edit_request",
                                bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()
                    }
                }.exhaustive
            }
        }

        if (addEditMomentViewModel.isDeleteAllow)
            setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.moment_item_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete_item -> {
                makeDeleteConfirmationDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeDeleteConfirmationDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setMessage("Вы уверены, что хотите удалить момент?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    addEditMomentViewModel.onDeleteMomentClick()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
        val alert = dialogBuilder.create()
        alert.show()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        addEditMomentViewModel.momentDate = sdf.format(calendar.time)
        addEditMomentViewModel.dateYear = year
        addEditMomentViewModel.dateMonth = monthOfYear
        addEditMomentViewModel.dateDay = dayOfMonth
        addEditMomentViewModel.dateLong =
                LocalDateTime.of(year, monthOfYear+1, dayOfMonth, 0, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        addEditMomentViewModel.onDateChanged()
    }
}