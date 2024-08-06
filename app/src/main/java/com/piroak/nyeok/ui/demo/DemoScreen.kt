package com.piroak.nyeok.ui.demo

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.location.DeviceOrientation
import com.piroak.nyeok.AppViewModelProvider
import com.piroak.nyeok.GlobalApplication
import com.piroak.nyeok.data.ILocationOrientationProvider
import com.piroak.nyeok.permission.IPermissionManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@Composable
fun DemoScreen(viewModel: DemoViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    // State from outside
    val locationGranted by viewModel.locationPermissionFlow.collectAsState()
    val orientation: DeviceOrientation? by viewModel.orientationFlow.collectAsState()
    val location: Location? by viewModel.locationFlow.collectAsState()
    
    // Composable local state
    var searchDialogOpen by remember { mutableStateOf(true) }

    Column {
        Button(onClick = viewModel::requestLocationPermission) {
            if (locationGranted) {
                Text("Location Permission 허용됨")
            } else {
                Text("Location Permission 가 필요합니다")
            }
        }

        Button(onClick = { searchDialogOpen = true }) {
            Text(text = "목적지 설정")
        }

        KakaoCircleMap(location = location, orientation = orientation, modifier = Modifier)
    }
    
    if (searchDialogOpen) {
        Dialog(
            onDismissRequest = { searchDialogOpen = false },
            DialogProperties(usePlatformDefaultWidth = false)
        ) {
            SearchDialog(modifier=Modifier.size(320.dp, 600.dp))
        }
    }
}

@Composable
fun SearchDialog(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    
    Surface(modifier=modifier) {
        Column() {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier=Modifier.fillMaxWidth()
            )
            Text(text = "Item 1")
            Text(text = "Item 1")
            Text(text = "Item 1")
            Text(text = "Item 1")
            Text(text = "Item 1")
        } 
    }
}

@Preview
@Composable
fun DemoScreenPreview() {
    val globalApplication = GlobalApplication()
    val permissionManager = object : IPermissionManager {
        override val locationPermissionFlow: StateFlow<Boolean>
            get() = flowOf(true).stateIn(
                globalApplication.applicationScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = true
            )

        override fun requestPermissions(requests: Array<String>) {
            TODO("Not yet implemented")
        }
    }
    
    val locationOrientationProvider = object : ILocationOrientationProvider {
        override val orientationFlow: StateFlow<DeviceOrientation?>
            get() = flowOf(null).stateIn(
                globalApplication.applicationScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = null
            )
        override val locationFlow: StateFlow<Location?>
            get() = flowOf(null).stateIn(
                globalApplication.applicationScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
                initialValue = null
            )
    }
    
    Surface(modifier = Modifier.fillMaxSize()) {
        val viewModel = DemoViewModel(locationOrientationProvider, permissionManager)

        DemoScreen(viewModel = viewModel)
    }
}