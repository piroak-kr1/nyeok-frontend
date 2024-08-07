package com.piroak.nyeok

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * A helper function to get same ViewModel across Screens in activity
 */
@Composable
inline fun <reified VM : ViewModel> appViewModel(): VM = viewModel<VM>(
    factory = AppViewModelProvider.Factory,
    viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
)