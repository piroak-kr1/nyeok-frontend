import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.plugin.serialization)
}

fun loadProperties(fileName: String) : Properties {
    val properties = Properties()
    properties.load(rootProject.file(fileName).inputStream())
    return properties
}

android {
    namespace = "com.piroak.nyeok"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.piroak.nyeok"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        
        // Load variables from properties file
        val secretProperties = loadProperties(".secret.properties")
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] = secretProperties.getProperty("KAKAO_NATIVE_APP_KEY")
        manifestPlaceholders["KAKAO_REST_API_KEY"] = secretProperties.getProperty("KAKAO_REST_API_KEY")
        
        ndk {
            // KakaoMap for Android only supports arm
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
//            abiFilters.add("x86")
//            abiFilters.add("x86_64")
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    
    // Testing (Default)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.compose.ui.test.junit4)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.navigation.dynamic.features.fragment)    
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json) // @Serializable
    androidTestImplementation(libs.androidx.navigation.testing)

    // Retrofit
    implementation(platform(libs.retrofit.bom))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.retrofit.converter.gson)
    
    // KakaoMap SDK
    implementation(libs.kakao.maps)
    implementation(libs.kakao.v2.common)
    
    // Google Play Services
    implementation(libs.play.services.location)
}