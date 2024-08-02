package com.piroak.nyeok

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.piroak.nyeok.ui.demo.DemoViewModel

object AppViewModelProvider {
    private var container: AppContainer? = null

    private fun CreationExtras.globalApplication(): GlobalApplication =
        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GlobalApplication)

    private fun CreationExtras.getContainer(): AppContainer {
        return container ?: globalApplication().container.also {
            container = it
        }
    }

    val Factory = viewModelFactory {
        initializer {
            getContainer().run {
                DemoViewModel(
                    permissionManager = permissionManager
                )
            }
        }
    }
}