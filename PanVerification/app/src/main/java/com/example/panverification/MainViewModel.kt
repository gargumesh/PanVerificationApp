package com.example.panverification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel:ViewModel() {

    private val _isNextButtonEnabled = MutableLiveData(false)
    val isNextButtonEnabled: LiveData<Boolean> get() = _isNextButtonEnabled

    private val _panError = MutableLiveData<String?>()
    val panError: LiveData<String?> get() = _panError

    private val _dayError = MutableLiveData<String?>()
    val dayError: LiveData<String?> get() = _dayError

    private val _monthError = MutableLiveData<String?>()
    val monthError: LiveData<String?> get() = _monthError

    private val _yearError = MutableLiveData<String?>()
    val yearError: LiveData<String?> get() = _yearError


    fun onInputChanged(pan: String, day: String, month: String, year: String) {
        viewModelScope.launch {
            val isPanValid = withContext(Dispatchers.Default) { validatePanNumber(pan) }
            val dayValidation = withContext(Dispatchers.Default) { validateDay(day) }
            val monthValidation = withContext(Dispatchers.Default) { validateMonth(month) }
            val yearValidation = withContext(Dispatchers.Default) { validateYear(year) }

            // Update error messages
            _panError.value = if (isPanValid) null else "Invalid PAN format"
            _dayError.value = dayValidation
            _monthError.value = monthValidation
            _yearError.value = yearValidation

            // Enable button
            _isNextButtonEnabled.value = isPanValid && dayValidation == null &&
                    monthValidation == null && yearValidation == null
        }
    }

    private suspend fun validatePanNumber(pan: String): Boolean {
        delay(100)
        val panPattern = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        val panPattern2 = Regex("[a-z]{5}[0-9]{4}[a-z]{1}")
        return pan.matches(panPattern) || pan.matches(panPattern2)
    }

    private suspend fun validateDay(day: String): String? {
        delay(50)
        val dayInt = day.toIntOrNull()
        return if (dayInt == null || dayInt !in 1..31) "Invalid day" else null
    }

    private suspend fun validateMonth(month: String): String? {
        delay(50)
        val monthInt = month.toIntOrNull()
        return if (monthInt == null || monthInt !in 1..12) "Invalid month" else null
    }

    private suspend fun validateYear(year: String): String? {
        delay(50)
        val yearInt = year.toIntOrNull()
        return if (yearInt == null || yearInt !in 1900..2023) "Invalid year" else null
    }
}