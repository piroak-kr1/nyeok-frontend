package com.piroak.nyeok

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.MapView
import com.piroak.nyeok.ui.theme.NyeokTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NyeokTheme { // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Text(text = "Hello, World!")
                        AndroidView(factory = { context ->
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
                                        print("Map is ready")
                                    }
                                })
                            }
                        })
                    }
                }
            }
        }
    }
}
