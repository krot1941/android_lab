package com.example.loversdiary.ui.statistic

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loversdiary.R
import com.example.loversdiary.data.SpinnerChoice
import com.example.loversdiary.databinding.StatisticFragmentBinding
import com.example.loversdiary.util.exhaustive
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class StatisticFragment : Fragment(R.layout.statistic_fragment) {

    private val statisticViewModel: StatisticViewModel by viewModels()

    private val statisticAdapter: StatisticAdapter = StatisticAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = StatisticFragmentBinding.bind(view)

        binding.apply {

            statisticFrgRecyclerList.apply {
                adapter = statisticAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            statisticViewModel.spinnerChoice.observe(viewLifecycleOwner) {
                statisticFrgSpinner.setSelection(it.spinnerChoice.ordinal)
            }

            statisticFrgSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, itemSelected: View?, selectedItemPosition: Int, selectedId: Long) {
                    if(itemSelected != null) {
                        if (selectedItemPosition == 0)
                            statisticFrgTextView.text = "Статистика за последнюю:"
                        else
                            statisticFrgTextView.text = "Статистика за последний:"
                        when(selectedItemPosition) {
                            0 -> statisticViewModel.onSpinnerChoiceSelected(SpinnerChoice.WEEK)
                            1 -> statisticViewModel.onSpinnerChoiceSelected(SpinnerChoice.MONTH)
                            2 -> statisticViewModel.onSpinnerChoiceSelected(SpinnerChoice.QUARTER)
                            3 -> statisticViewModel.onSpinnerChoiceSelected(SpinnerChoice.YEAR)
                        }
                    }
                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        }

        statisticViewModel.eventsList.observe(viewLifecycleOwner) {
            statisticAdapter.setStatistic(it)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            statisticViewModel.statisticsEvent.collect { event ->
                when (event) {
                    else -> {}
                }.exhaustive
            }
        }
    }
}