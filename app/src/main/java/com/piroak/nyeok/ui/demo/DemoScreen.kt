package com.piroak.nyeok.ui.demo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.piroak.nyeok.AppViewModelProvider
import com.piroak.nyeok.permission.PermissionManager

@Composable
fun DemoScreen(viewModel: DemoViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val locationGranted by viewModel.locationPermissionFlow.collectAsState()

    Column {
        Button(onClick = viewModel::requestLocationPermission) {
            if (locationGranted) {
                Text("Location Permission is already Granted")
            } else {
                Text("Request Location Permission")
            }
        }
    }
}

@Preview
@Composable
fun DemoScreenPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val permissionManager = PermissionManager(
            LocalContext.current
        )
        val viewModel = DemoViewModel(permissionManager)

        DemoScreen(viewModel = viewModel)
    }
}