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
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    // UI
//    implementation("androidx.appcompat:appcompat:1.4.1")
//    implementation("androidx.fragment:fragment-ktx:1.4.1")
//    implementation("androidx.recyclerview:recyclerview:1.2.1")
//    implementation("androidx.constraintlayout:constraintlayout:2.1.3")
//    implementation("com.google.android.material:material:1.5.0")
//    implementation("com.facebook.shimmer:shimmer:0.5.0")

    implementation(libs.lottie)
    implementation(libs.coil)

    // lifecycle
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
//    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycle_version")

    // navigation
//    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
//    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")
//    implementation("androidx.navigation:navigation-dynamic-features-fragment:$navigation_version")

    // DI
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
//    implementation("com.google.dagger:hilt-android:$hilt_version")
//    kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
//    implementation("androidx.hilt:hilt-navigation-fragment:1.0.0")

    // Reactive
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    //Jetpack Compose
//    implementation("androidx.compose.ui:ui:$compose_version")
//    implementation("androidx.compose.ui:ui-tooling:$compose_version") // Tooling support (Previews, etc.)
//    implementation("androidx.compose.foundation:foundation:$compose_version") // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
//    implementation("androidx.compose.material:material:$compose_version") // Material Design
//    implementation("androidx.compose.material:material-icons-core:$compose_version") // Material design icons
//    implementation("androidx.compose.material:material-icons-extended:$compose_version") // Integration with observables
//    implementation("androidx.compose.runtime:runtime-livedata:$compose_version")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1") // viewModels for Compose
//    implementation("androidx.activity:activity-compose:1.4.0")
//    implementation("androidx.navigation:navigation-compose:2.4.2")
//    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.24.7-alpha")
}