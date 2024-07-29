package com.piroak.nyeok

import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class NyeokApplication : Application() {
    // NOTE: Never changed after initialization
    lateinit var container: AppContainer
    lateinit var env: Env

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
        env = Env(kakaoNativeAppKey = getMetaDataValue("KAKAO_NATIVE_APP_KEY"))
    }

    private fun getMetaDataValue(name: String): String {
        val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(
            packageName, PackageManager.GET_META_DATA
        )
        val bundle = applicationInfo.metaData
        return bundle.getString(name)!!
    }
}

/** Dependencies Container Class
 *
 */
interface AppContainer {}

class DefaultAppContainer : AppContainer {}

