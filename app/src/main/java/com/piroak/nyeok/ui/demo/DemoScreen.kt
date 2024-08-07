package com.piroak.nyeok.ui.demo

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.location.DeviceOrientation
import com.kakao.vectormap.LatLng
import com.piroak.nyeok.GlobalApplication
import com.piroak.nyeok.R
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
    val userLocation: LatLng? by viewModel.locationFlow.collectAsState()
    val userHeadingDegrees: Float? by viewModel.userHeadingDegreesFlow.collectAsState()
    val destination: LatLng? by viewModel.destinationFlow.collectAsState()
    
    val straightPath: Pair<Float, Float>? by viewModel.straightPathFlow.collectAsState()
    val (distance, bearing) = straightPath ?: Pair(null, null)
    
    val compassDirection: Float? = if (userHeadingDegrees != null && bearing != null) {
        (bearing - userHeadingDegrees!!).let { 
            if (it < 0) it + 360 else it
        }
    } else {
        null
    }
    
    Column {
        Button(onClick = viewModel::requestLocationPermission) {
            if (locationGranted) {
                Text("Location Permission 허용됨")
            } else {
                Text("Location Permission 이 필요합니다")
            }
        }

        Column {
            Text(text = "목적지: ${destination ?: "설정되지 않음"}")
            Button(onClick = onSearchClick) {
                Text(text = "목적지 설정")
            }
        }
        
        HorizontalDivider()
        
        if (distance != null) {
            Text(text = "$distance m")
        }

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(300.dp)
                .clip(CircleShape)
                .border(width = 1.dp, color = Color.Black, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (compassDirection != null) {
                Image(
                    painter = painterResource(id = R.drawable.compass_128),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .rotate(compassDirection)
                )
            }
            KakaoCircleMap(
                location = userLocation,
                orientation = userHeadingDegrees,
                modifier = Modifier.fillMaxSize(fraction = 0.7f)
            )
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