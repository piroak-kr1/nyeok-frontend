package com.piroak.nyeok.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piroak.nyeok.network.AppApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SampleViewModel : ViewModel() {
    private val _uiState = MutableStateFlow("")
    val uiState: StateFlow<String> = _uiState.asStateFlow()

    fun sendEcho(message: String) {
        viewModelScope.launch {
            val response = AppApi.retrofitService.echo(message)
            _uiState.value = response
        }
    }
}