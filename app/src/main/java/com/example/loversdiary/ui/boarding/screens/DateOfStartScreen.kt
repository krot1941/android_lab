package com.example.loversdiary.ui.boarding.screens

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.loversdiary.R
import com.example.loversdiary.databinding.ViewPageDateOfStartFragmentBinding
import com.example.loversdiary.databinding.ViewPageSelfNameFragmentBinding
import com.example.loversdiary.ui.boarding.ViewPagerViewModel
import com.example.loversdiary.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class DateOfStartScreen(
        private val viewModel: ViewPagerViewModel
) : Fragment(R.layout.view_page_date_of_start_fragment) {

    private val calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ViewPageDateOfStartFragmentBinding.bind(view)

        binding.apply {

            dateOfStartTextEdit.text = viewModel.userDateOfStartView

            dateOfStartTextEdit.setOnClickListener {
                nextButtonDateOfStart.text = "завершить"
                DatePickerDialog(requireContext(), dateSetListener,
                        viewModel.dateYear,
                        viewModel.dateMonth-1,
                        viewModel.dateDay).show()
            }

            nextButtonDateOfStart.setOnClickListener {
                viewModel.onFinishDateOfStartClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.vewPagerEvent.collect { event ->
                when (event) {
                    is ViewPagerViewModel.ViewPagerEvent.ChangeDateView -> {
                        binding.dateOfStartTextEdit.text = viewModel.userDateOfStartView
                    }
                    else -> {}
                }.exhaustive
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        viewModel.userDateOfStartView = sdf.format(calendar.time)
        viewModel.userDateOfStart =
                LocalDateTime.of(year, monthOfYear+1, dayOfMonth, 0, 0, 0)
                .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        viewModel.dateYear = year
        viewModel.dateMonth = monthOfYear
        viewModel.dateDay = dayOfMonth
        viewModel.onDateChanged()
    }
}