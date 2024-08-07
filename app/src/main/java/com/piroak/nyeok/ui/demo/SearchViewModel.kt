package com.piroak.nyeok.ui.demo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class SearchViewModel : ViewModel() {
    private val _userInputFlow = MutableStateFlow("")
    val userInputFlow: StateFlow<String> = _userInputFlow

    fun onUserInputChanged(userInput: String) {
        _userInputFlow.value = userInput
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<String>> =
        userInputFlow.debounce(1000L).distinctUntilChanged().mapLatest { userInput: String ->
            processInput(userInput)
        }.stateIn(
            viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList()
        )

    private fun processInput(userInput: String): List<String> {
        Log.d("GUN", "processInput: $userInput")
        return listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    }
} 