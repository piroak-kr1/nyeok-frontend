package com.piroak.nyeok.ui.demo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.maps.routing.v2.Route
import com.google.maps.routing.v2.RouteLegStep
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TransitViewModel(
) : ViewModel() {
    private val _stepsListFlow: MutableStateFlow<List<RouteLegStep>?> =
        MutableStateFlow(getMockRoute().legsList[0].stepsList)
    private val _indexFlow: MutableStateFlow<Int> = MutableStateFlow(0)
    val currentStepFlow: StateFlow<RouteLegStep?> = makeStateFlow(
        initialValue = null,
        flow = _indexFlow.map { index ->
            _stepsListFlow.value?.getOrNull(index)
        },
    )

    fun info() = getMockRoute().legsList[0].stepsOverview!!

    fun nextStep() {
        val stepsList = _stepsListFlow.value
        if (stepsList.isNullOrEmpty()) return
        _indexFlow.value = (_indexFlow.value + 1).coerceAtMost(maximumValue = stepsList.lastIndex)
    }

    fun prevStep() {
        _indexFlow.value = (_indexFlow.value - 1).coerceAtLeast(minimumValue = 0)
    }
}