package com.piroak.nyeok.ui.view

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.location.DeviceOrientation
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapGravity
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.kakao.vectormap.label.Label
import com.kakao.vectormap.label.LabelLayer
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.TrackingManager



@Composable
fun KakaoCircleMap(
    location: Location?,
    orientation: DeviceOrientation?,
    modifier: Modifier = Modifier
) {
    var kakaoState: Pair<KakaoMap, Label>? by remember { mutableStateOf(null) }
    
    val userLocation: LatLng = if (location != null) {
        LatLng.from(location.latitude, location.longitude)
    } else LatLng.from(37.402005, 127.108621)

    val headingDegrees: Float = orientation?.headingDegrees ?: 0f
    val userRotation: Float = Math.toRadians(headingDegrees.toDouble()).toFloat()
    
    AndroidView(
        modifier = modifier
            .aspectRatio(ratio = 1f)
            .clip(CircleShape),
        factory = { context ->
            MapView(context).also {
                it.start(/* lifeCycleCallback = */ object : MapLifeCycleCallback() {
                    override fun onMapDestroy() {
                        TODO("Not yet implemented")
                    }
    
                    override fun onMapError(error: Exception?) {
                        TODO("Not yet implemented")
                    }
                }, /* ...readyCallbacks = */ object : KakaoMapReadyCallback() {
                    override fun onMapReady(kakaoMap: KakaoMap) {
                        // Called when API is executed normally after Authentication
                        
                        // Set kakao logo
                        kakaoMap.logo?.setPosition(
                            /* mapGravity = */  MapGravity.BOTTOM or MapGravity.CENTER_HORIZONTAL,
                            /* xPx = */ 0f,
                            /* yPx = */ 40f
                        )
                        
                        // Create Label
                        val labelManager = kakaoMap.labelManager!!
                        val labelStyles:LabelStyles = labelManager.addLabelStyles(
                            LabelStyles.from(LabelStyle.from(android.R.drawable.arrow_up_float))
                        )!!
                        val options:LabelOptions = LabelOptions.from(userLocation).setStyles(labelStyles)
                        val layer:LabelLayer = kakaoMap.labelManager!!.layer!!
                        val userLabel = layer.addLabel(options)
                        
                        // Start tracking userLabel
                        val trackingManager: TrackingManager = kakaoMap.trackingManager!!
                        trackingManager.startTracking(userLabel)
                        trackingManager.setTrackingRotation(true)

                        kakaoState = Pair(kakaoMap, userLabel)
                    }
                })
            }
        }, update = { _mapView -> 
            Log.d("GUN", "KakaoCircleMap: update")
            
            if (kakaoState != null) {
                val (kakaoMap:KakaoMap, userLabel:Label) = kakaoState!!
                userLabel.moveTo(userLocation)
                userLabel.rotateTo(userRotation)
            }
        }
    )
}
