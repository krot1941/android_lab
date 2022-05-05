package com.example.loversdiary.ui.statistic

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.loversdiary.data.EventDao
import com.example.loversdiary.data.PreferencesManager
import com.example.loversdiary.data.SpinnerChoice
import com.example.loversdiary.util.exhaustive
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class StatisticViewModel  @ViewModelInject constructor(
        private val eventDao: EventDao,
        private val preferencesManager: PreferencesManager,
) : ViewModel() {


    private val preferencesFlow = preferencesManager.preferencesFlow

    val spinnerChoice = preferencesFlow.asLiveData()

    val eventsList = spinnerChoice.asFlow().flatMapLatest { choice ->
        eventDao.getEventsStatistic(
            when (choice.spinnerChoice) {
                SpinnerChoice.WEEK ->  System.currentTimeMillis() - 6.048e+8
                SpinnerChoice.MONTH -> System.currentTimeMillis() - 2.628e+9
                SpinnerChoice.QUARTER -> System.currentTimeMillis() - 3*2.628e+9
                SpinnerChoice.YEAR -> System.currentTimeMillis() - 3.154e+10
            }.exhaustive.toLong()
        )
    }.asLiveData()

    private val statisticsEventChannel = Channel<StatisticsEvent>()
    val statisticsEvent = statisticsEventChannel.receiveAsFlow()

    fun onSpinnerChoiceSelected(choice: SpinnerChoice) = viewModelScope.launch {
        preferencesManager.updateSpinnerChoice(choice)
    }

    sealed class StatisticsEvent {}
}