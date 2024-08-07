package com.piroak.nyeok.ui.demo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.piroak.nyeok.GlobalApplication
import com.piroak.nyeok.network.Document
import com.piroak.nyeok.network.KakaoLocalApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

class SearchViewModel(
    private val globalApplication: GlobalApplication,
    private val kakaoLocalApiService: KakaoLocalApiService
) : ViewModel() {
    private val _userInputFlow = MutableStateFlow("")
    val userInputFlow: StateFlow<String> = _userInputFlow

    fun onUserInputChanged(userInput: String) {
        _userInputFlow.value = userInput
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val searchResults: StateFlow<List<Document>> =
        userInputFlow.debounce(1000L).distinctUntilChanged().mapLatest { userInput: String ->
            if (userInput.isEmpty()) emptyList() else queryKakaoLocalApi(userInput)
        }.stateIn(
            viewModelScope, started = SharingStarted.WhileSubscribed(), initialValue = emptyList()
        )

    private suspend fun queryKakaoLocalApi(userInput: String): List<Document> {
        if (userInput.isEmpty()) return emptyList()
        return kakaoLocalApiService.searchByKeyword(
            restApiKey = "KakaoAK ${globalApplication.env.kakaoRestApiKey}",
            keywordQuery = userInput
        ).documents
    }
} 