package com.example.loversdiary.ui.addeditmoment

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loversdiary.data.Event
import com.example.loversdiary.data.EventDao
import com.example.loversdiary.data.Moment
import com.example.loversdiary.data.MomentDao
import com.example.loversdiary.ui.ADD_MOMENT_RESULT_OK
import com.example.loversdiary.ui.DELETE_MOMENT_RESULT_OK
import com.example.loversdiary.ui.EDIT_MOMENT_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddEditMomentViewModel @ViewModelInject constructor(
        private val momentDao: MomentDao,
        private val eventDao: EventDao,
        @Assisted private val state: SavedStateHandle,
) : ViewModel() {

    private val moment = state.get<Moment>("moment")
    private var event = state.get<Event>("event")

    val isDeleteAllow = moment != null

    val eventsList = eventDao.getAllEvents()
    val stringEventList = getStrEventList().toList()
    var momentEvent = event ?: eventsList[0]

    var dateLong: Long = moment?.date ?: System.currentTimeMillis()
    var dateYear: Int = moment?.dateFormattedAsYear?.toInt() ?:
    SimpleDateFormat("yyyy", Locale.getDefault()).format(Date(dateLong)).toInt()
    var dateMonth: Int = moment?.dateFormattedAsMonth?.toInt() ?:
    SimpleDateFormat("MM", Locale.getDefault()).format(Date(dateLong)).toInt()
    var dateDay: Int = moment?.dateFormattedAsDay?.toInt() ?:
    SimpleDateFormat("dd", Locale.getDefault()).format(Date(dateLong)).toInt()

    var momentPlace = state.get<String>("momentPlace") ?: moment?.place ?: ""
        set(value) {
            field = value
            state.set("momentPlace", value)
        }
    var momentNote = state.get<String>("momentNote") ?: moment?.note ?: ""
        set(value) {
            field = value
            state.set("momentNote", value)
        }
    var momentDate: String = state.get<String>("momentDate") ?: moment?.dateFormatted ?:
    SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(dateLong))
        set(value) {
            field = value
            state.set("momentDate", value)
        }

    private val addEditMomentEventChannel = Channel<AddEditMomentEvent>()
    val addEditMomentEvent = addEditMomentEventChannel.receiveAsFlow()

    private fun getStrEventList(): MutableList<String> {
        val newList = mutableListOf<String>()
        eventsList.forEach {newList.add(it.name)}
        return newList
    }

    fun onSaveMomentClick() {
        if (momentPlace.isBlank()) {
            showInvalidInputMessage("Место не может быть пустым")
            return
        }
        if (momentNote.isBlank()) {
            showInvalidInputMessage("Запись момента не может быть пустой")
            return
        }

        if (moment != null) {

            val updatedMoment = moment.copy(
                    place = momentPlace,
                    note = momentNote,
                    date = dateLong,
                    event_id = momentEvent.id
            )
            event = momentEvent
            state.set("event", momentEvent)
            updateMoment(updatedMoment)
        }
        else {

            val newMoment = Moment(
                place = momentPlace,
                note = momentNote,
                date = dateLong,
                event_id = momentEvent.id
            )
            event = momentEvent
            state.set("event", momentEvent)
            createMoment(newMoment)
        }
    }

    private fun updateMoment(moment: Moment) = viewModelScope.launch {
        momentDao.update(moment)
        addEditMomentEventChannel.send(AddEditMomentEvent.NavigateBackWithResult(EDIT_MOMENT_RESULT_OK))
    }

    private fun createMoment(moment: Moment) = viewModelScope.launch {
        momentDao.insert(moment)
        addEditMomentEventChannel.send(AddEditMomentEvent.NavigateBackWithResult(ADD_MOMENT_RESULT_OK))
    }

    private fun deleteMoment(moment: Moment) = viewModelScope.launch {
        momentDao.delete(moment)
        addEditMomentEventChannel.send(AddEditMomentEvent.NavigateBackWithResult(DELETE_MOMENT_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        addEditMomentEventChannel.send(AddEditMomentEvent.ShowInvalidInputMessage(text))
    }

    fun onDateChanged() = viewModelScope.launch {
        addEditMomentEventChannel.send(AddEditMomentEvent.ChangeDateView)
    }

    fun onDeleteMomentClick() {
        deleteMoment(moment!!)
    }

    sealed class AddEditMomentEvent {
        object ChangeDateView : AddEditMomentEvent()
        data class ShowInvalidInputMessage(val msg: String) : AddEditMomentEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditMomentEvent()
    }
}