package com.piroak.nyeok.ui.demo

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.location.DeviceOrientation
import com.kakao.vectormap.LatLng
import com.piroak.nyeok.GlobalApplication
import com.piroak.nyeok.appViewModel
import com.piroak.nyeok.data.ILocationOrientationProvider
import com.piroak.nyeok.permission.IPermissionManager
import com.piroak.nyeok.ui.view.KakaoCircleMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@Composable
fun DemoScreen(
    viewModel: DemoViewModel = appViewModel(),
    onSearchClick: () -> Unit = {}
) {
    // State from outside
    val locationGranted:Boolean by viewModel.locationPermissionFlow.collectAsState()
    val location: LatLng? by viewModel.locationFlow.collectAsState()
    val orientation: Float? by viewModel.orientationFlow.collectAsState()
    val destination: LatLng? by viewModel.destinationFlow.collectAsState()
    
    Column {
        Button(onClick = viewModel::requestLocationPermission) {
            if (locationGranted) {
                Text("Location Permission 허용됨")
            } else {
                Text("Location Permission 이 필요합니다")
            }
        }

        Row {
            Text(text = "목적지: ${destination ?: "설정되지 않음"}")
            Button(onClick = onSearchClick) {
                Text(text = "목적지 설정")
            }
        }

        KakaoCircleMap(location = location, orientation = orientation, modifier = Modifier)
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
        override val deviceOrientationFlow: StateFlow<DeviceOrientation?>
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