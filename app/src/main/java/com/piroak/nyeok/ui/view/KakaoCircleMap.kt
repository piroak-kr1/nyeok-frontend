package com.piroak.nyeok.ui.view

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapGravity
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView


@Composable
fun KakaoCircleMap(modifier: Modifier = Modifier) {
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
                        kakaoMap.logo?.setPosition(
                            /* mapGravity = */  MapGravity.BOTTOM or MapGravity.CENTER_HORIZONTAL,
                            /* xPx = */ 0f,
                            /* yPx = */ 40f
                        )
                    }
                })
            }
        }, update = {
        }
    )
}
