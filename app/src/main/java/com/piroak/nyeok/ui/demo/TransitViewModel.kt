package com.piroak.nyeok.ui.demo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TransitViewModel(
) : ViewModel() {
    fun xx() {
        viewModelScope.launch {
            val data = getMockRoute()
            Log.d("GUN", data.toString())
        }
    }
}