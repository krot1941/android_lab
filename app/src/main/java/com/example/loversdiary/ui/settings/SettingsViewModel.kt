package com.example.loversdiary.ui.settings

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loversdiary.data.User
import com.example.loversdiary.data.UserDao
import com.example.loversdiary.ui.EDIT_SETTINGS_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SettingsViewModel @ViewModelInject constructor(
        private val userDao: UserDao
) : ViewModel(){

    private val user = userDao.getUserToEdit()

    var userSelfName = user.user_name
    var userPartnerName = user.partner_name
    var dateOfStartView = user.dateOfStartFormatted
    var dateOfStartLong = user.date_of_start
    var dateDay = user.dateOfStartFormattedAsDay.toInt()
    var dateMonth = user.dateOfStartFormattedAsMonth.toInt()
    var dateYear = user.dateOfStartFormattedAsYear.toInt()

    private val settingsEventChannel = Channel<SettingsEvent>()
    val settingsEvent = settingsEventChannel.receiveAsFlow()

    fun onSubmitFABClick() {

        if (userSelfName.isBlank()) {
            showInvalidInputMessage("Ваше имя не может быть пустым")
            return
        }
        if (userPartnerName.isBlank()) {
            showInvalidInputMessage("Имя партнера не может быть пустым")
            return
        }

        val updatedUser = user.copy(
                user_name = userSelfName,
                partner_name = userPartnerName,
                date_of_start = dateOfStartLong
        )

        updateUser(updatedUser)
    }

    fun onDateChanged() = viewModelScope.launch {
        settingsEventChannel.send(SettingsEvent.ChangeDateView)
    }

    private fun updateUser(user: User) = viewModelScope.launch {
        userDao.update(user)
        settingsEventChannel.send(SettingsEvent.NavigateBackWithResult(EDIT_SETTINGS_RESULT_OK))
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        settingsEventChannel.send(SettingsEvent.ShowInvalidInputMessage(text))
    }

    sealed class SettingsEvent {
        object ChangeDateView : SettingsEvent()
        data class ShowInvalidInputMessage(val msg: String) : SettingsEvent()
        data class NavigateBackWithResult(val result: Int) : SettingsEvent()
    }
}