package com.example.loversdiary.ui.settings

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.loversdiary.R
import com.example.loversdiary.databinding.SettingsFragmentBinding
import com.example.loversdiary.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private val settingsViewModel: SettingsViewModel by viewModels()

    private val calendar = Calendar.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = SettingsFragmentBinding.bind(view)

        binding.apply {

            settingsFrgSelfNameView.setText(settingsViewModel.userSelfName)
            settingsFrgPartnerNameView.setText(settingsViewModel.userPartnerName)
            settingsFrgDateView.text = settingsViewModel.dateOfStartView

            settingsFrgSelfNameView.addTextChangedListener {
                settingsViewModel.userSelfName = it.toString()
            }

            settingsFrgPartnerNameView.addTextChangedListener {
                settingsViewModel.userPartnerName = it.toString()
            }

            settingsFrgDateView.setOnClickListener {
                DatePickerDialog(requireContext(), dateSetListener,
                        settingsViewModel.dateYear,
                        settingsViewModel.dateMonth - 1,
                        settingsViewModel.dateDay).show()
            }

            fabSubmit.setOnClickListener {
                settingsViewModel.onSubmitFABClick()
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            settingsViewModel.settingsEvent.collect { event ->
                when (event) {
                    is SettingsViewModel.SettingsEvent.ChangeDateView -> {
                        binding.settingsFrgDateView.text = settingsViewModel.dateOfStartView
                    }
                    is SettingsViewModel.SettingsEvent.ShowInvalidInputMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                    }
                    is SettingsViewModel.SettingsEvent.NavigateBackWithResult -> {
                        setFragmentResult("settings_request",
                                bundleOf("settings_request" to event.result)
                        )
                        findNavController().popBackStack()
                    }
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
        settingsViewModel.dateOfStartView = sdf.format(calendar.time)
        settingsViewModel.dateOfStartLong =
                LocalDateTime.of(year, monthOfYear+1, dayOfMonth, 0, 0, 0)
                        .atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        settingsViewModel.dateYear = year
        settingsViewModel.dateMonth = monthOfYear
        settingsViewModel.dateDay = dayOfMonth
        settingsViewModel.onDateChanged()
    }
}