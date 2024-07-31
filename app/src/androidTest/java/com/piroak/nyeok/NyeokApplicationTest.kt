package com.piroak.nyeok

import android.util.Log
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kakao.sdk.common.util.Utility
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NyeokApplicationTest {
    private lateinit var application: NyeokApplication

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext() as NyeokApplication
    }

    @Test
    fun printEnvToLogcat() {
        Log.i("NyeokApplicationTest", "Printing Env")
        Log.i("NyeokApplicationTest", application.env.toString())
        // NOTE: We cannot test secret values
    }
    
    @Test
    fun printKeyHashToLogcat() {
        Log.i("NyeokApplicationTest", "Printing KeyHash")
        Log.i("NyeokApplicationTest", Utility.getKeyHash(context = application))
    }
}