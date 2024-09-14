package com.piroak.nyeok.ui.demo

import android.location.Location
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.android.gms.location.DeviceOrientation
import com.google.maps.routing.v2.RouteLegStep
import com.google.maps.routing.v2.RouteTravelMode
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

fun Color.Companion.fromHex(sharpHexString: String): Color =
    Color(android.graphics.Color.parseColor(sharpHexString))

@Composable
fun DemoScreen(
    viewModel: DemoViewModel = appViewModel(),
    onSearchClick: () -> Unit = {}
) {
    // State from outside
    val locationGranted:Boolean by viewModel.locationPermissionFlow.collectAsState()
    val userLocation: LatLng? by viewModel.locationFlow.collectAsState()
    val userHeadingDegrees: Float? by viewModel.userHeadingDegreesFlow.collectAsState()

    val currentStep: RouteLegStep? by viewModel.currentStepFlow.collectAsState()
    val straightPath: Pair<Float, Float>? by viewModel.straightPathFlow.collectAsState()
    val (distance, bearing) = straightPath ?: Pair(null, null)
    
    val compassDirection: Float? = if (userHeadingDegrees != null && bearing != null) {
        (bearing - userHeadingDegrees!!).let { 
            if (it < 0) it + 360 else it
        }
    } else {
        null
    }
    
    val destination: LatLng? by viewModel.destinationFlow.collectAsState()
    
    
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
        
        Row {
            Button(onClick = viewModel::prevStep) {
                Text(text = "Prev Step")
            }
            Button(onClick = viewModel::nextStep) {
                Text(text = "Next Step")
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
        
        if (currentStep != null) {
            if (currentStep!!.travelMode == RouteTravelMode.TRANSIT) {
                BusDetail(step = currentStep!!)
            } else if (currentStep!!.travelMode == RouteTravelMode.WALK) {
                Text(text = "WALK")
            }
        }
    }
}

@Composable
fun BusDetail(step: RouteLegStep) {
    Column {
        step.transitDetails.let {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AsyncImage(
                            // iconUri example: "//maps.gstatic.com/mapfiles/transit/iw2/6/bus2.png"
                            model = "https:${it.transitLine.vehicle.iconUri}",
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )

                        Text(text = it.transitLine.nameShort, fontSize = 20.sp, color = Color.fromHex(/* colorString = */ it.transitLine.textColor), 
                            modifier = Modifier
                                .background(Color.fromHex(it.transitLine.color))
                                .padding(4.dp))
                    }

                    Text("승차: ${it.stopDetails.departureStop.name}")
                    Text("${step!!.localizedValues.staticDuration.text}, ${it.stopCount} 정류장 이동")
                    Text("하차: ${it.stopDetails.arrivalStop.name}")
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