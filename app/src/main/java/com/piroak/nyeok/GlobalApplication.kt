package com.piroak.nyeok

import android.app.Activity
import android.app.Application
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.kakao.vectormap.KakaoMapSdk
import com.piroak.nyeok.data.LocationOrientationProvider
import com.piroak.nyeok.permission.PermissionManager
import kotlinx.coroutines.MainScope

class GlobalApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = MainScope()
    var currentActivity: ComponentActivity? = null

    // NOTE: Never change container & env after initialization
    lateinit var container: AppContainer
    lateinit var env: Env

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(globalApplication = this)
        env = Env(
            kakaoNativeAppKey = getMetaDataValue("KAKAO_NATIVE_APP_KEY"),
            kakaoRestApiKey = getMetaDataValue("KAKAO_REST_API_KEY"),
        )
        KakaoMapSdk.init(/* context = */ this, /* appKey = */ env.kakaoNativeAppKey)

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
    }

    // NOTE: This object is used to get the current activity
    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            currentActivity = activity as ComponentActivity
        }

        override fun onActivityStarted(activity: Activity) {
            currentActivity = activity as ComponentActivity
        }

        override fun onActivityResumed(activity: Activity) {
            currentActivity = activity as ComponentActivity
        }

        override fun onActivityPaused(activity: Activity) {
            currentActivity = activity as ComponentActivity
        }

        override fun onActivityStopped(activity: Activity) {
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
        }
    }

    private fun getMetaDataValue(name: String): String {
        val applicationInfo: ApplicationInfo = packageManager.getApplicationInfo(
            packageName, PackageManager.GET_META_DATA
        )
        return applicationInfo.metaData.getString(name)!!
    }
}

/** Dependencies Container Class
 *
 */
interface AppContainer {
    val locationOrientationProvider: LocationOrientationProvider
    val permissionManager: PermissionManager
}

class DefaultAppContainer(globalApplication: GlobalApplication) : AppContainer {
    override val locationOrientationProvider: LocationOrientationProvider by lazy {
        LocationOrientationProvider(
            externalScope = globalApplication.applicationScope,
            globalApplication = globalApplication,
            permissionManager = permissionManager
        )
    }
    override val permissionManager: PermissionManager by lazy {
        PermissionManager(globalApplication = globalApplication)
    }
}
