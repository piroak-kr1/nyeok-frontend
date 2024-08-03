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
import com.google.android.gms.location.DeviceOrientation
import com.piroak.nyeok.AppViewModelProvider
import com.piroak.nyeok.GlobalApplication
import com.piroak.nyeok.data.LocationOrientationProvider
import com.piroak.nyeok.permission.PermissionManager

@Composable
fun DemoScreen(viewModel: DemoViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val locationGranted by viewModel.locationPermissionFlow.collectAsState()
    val orientation: DeviceOrientation? by viewModel.orientationFlow.collectAsState()

    Column {
        Text(text = "Orientation: $orientation")
        
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
    val globalApplication = LocalContext.current as GlobalApplication
    Surface(modifier = Modifier.fillMaxSize()) {
        val permissionManager = PermissionManager(
            globalApplication
        )
        val locationOrientationProvider = LocationOrientationProvider(
            externalScope = globalApplication.applicationScope,
            globalApplication = globalApplication,
            permissionManager = permissionManager
        )
        val viewModel = DemoViewModel(locationOrientationProvider, permissionManager)

        DemoScreen(viewModel = viewModel)
    }
}