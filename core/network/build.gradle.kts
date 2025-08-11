import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.plugin.serialization)
}

android {
    namespace = "com.combo.runcombi.network"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", getBaseUrl())
        }

        release {
            buildConfigField("String", "BASE_URL", getBaseUrl())
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    
    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.bundles.test)
    implementation(libs.bundles.network)
    implementation(libs.kotlin.serialization.json)
    implementation(libs.bundles.coroutines)

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
}

fun getBaseUrl(): String {
    val baseUrl = gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
    return if (baseUrl.isNullOrBlank()) {
        "http://api.runcombi.site/"
    } else {
        baseUrl
    }
}