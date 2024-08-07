package com.piroak.nyeok.ui.view

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
import com.kakao.vectormap.label.TransformMethod

@Composable
fun KakaoCircleMap(
    location: LatLng?,
    orientation: Float?,
    modifier: Modifier = Modifier
) {
    var kakaoState: Pair<KakaoMap, Label>? by remember { mutableStateOf(null) }
    
    val userLocation: LatLng = location ?: LatLng.from(37.402005, 127.108621)
    val userRotation: Float = if (orientation != null) { Math.toRadians((orientation).toDouble()).toFloat() } else 0f 
    
    AndroidView(
        modifier = modifier
            .aspectRatio(ratio = 1f)
            .clip(CircleShape),
        factory = { context ->
            MapView(context).also {
                it.start(/* lifeCycleCallback = */ object : MapLifeCycleCallback() {
                    override fun onMapDestroy() {
                        Log.e("GUN: onMapDestroy", "Destroyed")
                        kakaoState = null
                    }
    
                    override fun onMapError(error: Exception?) {
                        Log.e("GUN: onMapError", error.toString())
                        kakaoState = null
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
                            LabelStyles.from(
//                                LabelStyle.from(android.R.drawable.star_on)
                                LabelStyle.from(com.piroak.nyeok.R.drawable.current_location_10_128)
                                    .setAnchorPoint(0.5f, 0.5f)
                            )
                        )!!
                        val options:LabelOptions = LabelOptions.from(userLocation)
                            // label stays physically when map is rotated
                            .setTransform(TransformMethod.AbsoluteRotation)
                            .setStyles(labelStyles)
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
            if (kakaoState != null) {
                val (kakaoMap:KakaoMap, userLabel:Label) = kakaoState!!
                userLabel.moveTo(userLocation)
                userLabel.rotateTo(userRotation)
            }
        }
    )
}
