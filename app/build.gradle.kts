plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
//    alias(libs.plugins.google.services)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.antsfamily.biketrainer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.antsfamily.biketrainer"
        minSdk = 26
        targetSdk = 35
        val majorVersion = "0"
        val minorVersion = "1"
        val versionCode = if (System.getenv("GITHUB_ACTIONS") == "true") {
            System.getenv("GITHUB_RUN_NUMBER")?.toIntOrNull() ?: 1
        } else {
            1
        }

        versionName = "$majorVersion.$minorVersion.$versionCode"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
            isDebuggable = true
            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
        }
        release {
            isMinifyEnabled = false
            isDebuggable = false
            buildConfigField("String","VERSION_NAME","\"${defaultConfig.versionName}\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    lint {
        disable += "NullSafeMutableLiveData"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":android-ant-lib"))
    implementation(project(":antplugin-lib"))
    implementation(project(":garmin-fit"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.activity.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.lottie)
    implementation(libs.coil)

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Reactive
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")


}