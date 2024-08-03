import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
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
    // Default
    implementation("androidx.core:core-ktx:1.13.1")
    val lifeCycleVersion = "2.8.4"
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifeCycleVersion")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.06.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.06.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")    

    // Retrofit
    implementation(platform("com.squareup.retrofit2:retrofit-bom:2.11.0"))
    implementation("com.squareup.retrofit2:retrofit")
    // Retrofit with Scalar Converter
    implementation("com.squareup.retrofit2:converter-scalars")
    // Retrofit with Gson Converter
    implementation("com.squareup.retrofit2:converter-gson")
    
    // KakaoMap SDK
    implementation("com.kakao.maps.open:android:2.9.5")
    implementation("com.kakao.sdk:v2-common:2.20.3")
    
    // Google Play Services
    implementation("com.google.android.gms:play-services-location:21.3.0")
}