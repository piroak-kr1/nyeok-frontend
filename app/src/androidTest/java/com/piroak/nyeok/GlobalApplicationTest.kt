package com.piroak.nyeok

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kakao.sdk.common.util.Utility
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GlobalApplicationTest {
    private val application: GlobalApplication = ApplicationProvider.getApplicationContext() as GlobalApplication

    @Test
    fun printEnvToLogcat() {
        Log.i("GUN: NyeokApplicationTest", "Printing Env")
        Log.i("GUN: NyeokApplicationTest", application.env.toString())
        // NOTE: We cannot test secret values
    }
    
    @Test
    fun printKeyHashToLogcat() {
        Log.i("GUN: NyeokApplicationTest", "Printing KeyHash")
        Log.i("GUN: NyeokApplicationTest", Utility.getKeyHash(context = application))
    }
}