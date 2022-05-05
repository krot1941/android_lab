package com.example.loversdiary.ui.boarding

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.loversdiary.data.User
import com.example.loversdiary.data.UserDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ViewPagerViewModel @ViewModelInject constructor(
        private val userDao: UserDao,
        @Assisted private val state: SavedStateHandle
)  : ViewModel() {

    var userSelfName: String = ""
    var userPartnerName: String = ""
    var userDateOfStart: Long = System.currentTimeMillis()

    var userDateOfStartView: String = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            .format(Date(userDateOfStart))

    var dateYear: Int = SimpleDateFormat("yyyy", Locale.getDefault())
            .format(Date(userDateOfStart)).toInt()
    var dateMonth: Int = SimpleDateFormat("MM", Locale.getDefault())
            .format(Date(userDateOfStart)).toInt()
    var dateDay: Int = SimpleDateFormat("dd", Locale.getDefault())
            .format(Date(userDateOfStart)).toInt()


    private val viewPagerEventChannel = Channel<ViewPagerEvent>()
    val vewPagerEvent = viewPagerEventChannel.receiveAsFlow()

    fun onNextSelfNameClick() {
        if (userSelfName.isBlank()) {
            showInvalidInputMessage("Имя не может быть пустым")
            return
        }
        navigateToNextScreen()
    }

    fun onNextPartnerNameClick() {
        if (userPartnerName.isBlank()) {
            showInvalidInputMessage("Имя не может быть пустым")
            return
        }
        navigateToNextScreen()
    }

    fun onFinishDateOfStartClick() {
        val newUser = User(
                user_name = userSelfName,
                partner_name = userPartnerName,
                date_of_start = userDateOfStart
        )
        createUser(newUser)
    }

    private fun createUser(user: User) = viewModelScope.launch {
        userDao.insert(user)
        viewPagerEventChannel.send(ViewPagerEvent.FinishBoarding)
    }

    private fun navigateToNextScreen() = viewModelScope.launch {
        viewPagerEventChannel.send(ViewPagerEvent.NavigateToNextScreen)
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        viewPagerEventChannel.send(ViewPagerEvent.ShowInvalidInputMessage(text))
    }

    fun onDateChanged() = viewModelScope.launch {
        viewPagerEventChannel.send(ViewPagerEvent.ChangeDateView)
    }

    sealed class ViewPagerEvent {
        object NavigateToNextScreen : ViewPagerEvent()
        object FinishBoarding : ViewPagerEvent()
        object ChangeDateView : ViewPagerEvent()
        data class ShowInvalidInputMessage(val msg: String) : ViewPagerEvent()
    }
}