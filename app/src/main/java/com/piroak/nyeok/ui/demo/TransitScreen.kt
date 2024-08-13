package com.piroak.nyeok.ui.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.maps.routing.v2.RouteLegStep
import com.google.maps.routing.v2.RouteTravelMode
import com.piroak.nyeok.appViewModel
import com.piroak.nyeok.common.LatLng
import com.piroak.nyeok.network.toCommonLatLng

fun Color.Companion.fromHex(hex: String): Color {
    val colorInt = android.graphics.Color.parseColor(hex)
    return Color(colorInt)
}

@Composable
fun TransitScreen(
    viewModel: TransitViewModel = appViewModel()
) {
    val currentStep: RouteLegStep? by viewModel.currentStepFlow.collectAsState()

    Column {
        Row {
            Button(onClick = viewModel::prevStep) {
                Text(text = "Prev")
            }
            Button(onClick = viewModel::nextStep) {
                Text(text = "Next")
            }
        }

        if (currentStep != null) {
            if (currentStep!!.travelMode == RouteTravelMode.TRANSIT) {
                currentStep!!.transitDetails.let {
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
                    Text("${currentStep!!.localizedValues.staticDuration.text}, ${it.stopCount} 정류장 이동")
                    Text("하차: ${it.stopDetails.arrivalStop.name}")
                }
            } else if (currentStep!!.travelMode == RouteTravelMode.WALK) {
                val destination: LatLng = currentStep!!.endLocation.latLng.toCommonLatLng()
                Text("WALK")
            }
        }
    }
}