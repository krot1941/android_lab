package com.example.loversdiary.ui.momentslist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.loversdiary.data.Event
import com.example.loversdiary.data.EventDao
import com.example.loversdiary.data.Moment
import com.example.loversdiary.data.MomentDao
import com.example.loversdiary.ui.ADD_MOMENT_RESULT_OK
import com.example.loversdiary.ui.DELETE_MOMENT_RESULT_OK
import com.example.loversdiary.ui.EDIT_MOMENT_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MomentsListViewModel @ViewModelInject constructor(
        private val momentDao: MomentDao,
        private val eventDao: EventDao
) : ViewModel() {

    val dataList = combine(
        momentDao.getAllMoments(),
        eventDao.getEventsByMoments()
    ){ moments, events ->
        Pair(moments, events)
    }.asLiveData()

    private val momentsListEventChannel = Channel<MomentsListEvent>()
    val momentsListEvent = momentsListEventChannel.receiveAsFlow()

    fun onAddMomentClick() = viewModelScope.launch {
        momentsListEventChannel.send(MomentsListEvent.NavigateToAddMoment)
    }

    fun onMomentItemSelected(moment: Moment, event: Event) = viewModelScope.launch {
        momentsListEventChannel.send(MomentsListEvent.NavigateToEditMoment(moment, event))
    }

    private fun showMomentSavedConfirmationMessage(text: String) = viewModelScope.launch {
        momentsListEventChannel.send(MomentsListEvent.ShowMomentSavedConfirmationMessage(text))
    }

    fun onAddEditResult(result: Int) {
        when (result) {
            ADD_MOMENT_RESULT_OK -> showMomentSavedConfirmationMessage("Момент успешно добавлен")
            EDIT_MOMENT_RESULT_OK -> showMomentSavedConfirmationMessage("Момент успешно обновлен")
            DELETE_MOMENT_RESULT_OK -> showMomentSavedConfirmationMessage("Момент успешно удален")
        }
    }

    sealed class MomentsListEvent {
        object NavigateToAddMoment : MomentsListEvent()
        data class NavigateToEditMoment(val moment: Moment, val event: Event) : MomentsListEvent()
        data class ShowMomentSavedConfirmationMessage(val msg: String) : MomentsListEvent()
    }
}