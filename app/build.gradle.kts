import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("runcombi.android.application")
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.combo.runcombi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.combo.runcombi"
        minSdk = 26
        targetSdk = 35
        versionCode = 109
        versionName = "1.0.9"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val googleApiKey = gradleLocalProperties(rootDir, providers).getProperty("GOOGLE_API_KEY") ?: ""
        manifestPlaceholders["googleApiKey"] = googleApiKey

        val kakaoApiKey = gradleLocalProperties(rootDir, providers).getProperty("KAKAO_API_KEY") ?: ""
        manifestPlaceholders["kakaoApiKey"] = kakaoApiKey
        buildConfigField("String", "KAKAO_API_KEY", "\"$kakaoApiKey\"")
    }

    signingConfigs {
        create("release") {
            storeFile = file("./runcombi-keystore.jks")
            storePassword = gradleLocalProperties(rootDir, providers).getProperty("SIGNING_STORE_PASSWORD")
            keyAlias =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_ALIAS")
            keyPassword =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
        }
        debug {
            isDebuggable = true
        }
    }
    
    buildFeatures {
        buildConfig = true
    }

    flavorDimensions += "mode"
    productFlavors {
        create("mock") {
            dimension = "mode"
        }
        create("prod") {
            dimension = "mode"
        }
    }
}

dependencies {
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)
    implementation(libs.v2.user)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    implementation(project(":feature:main"))
    implementation(project(":feature:login"))
    implementation(project(":feature:history"))
    implementation(project(":feature:setting"))
    implementation(project(":feature:walk"))

    implementation(project(":core:designsystem"))

    implementation(project(":core:data:common"))
    implementation(project(":core:data:auth"))
    implementation(project(":core:data:user"))
    implementation(project(":core:data:walk"))
    implementation(project(":core:data:history"))
    implementation(project(":core:data:setting"))

    // Test dependencies
    testImplementation(libs.junit)
    testImplementation(libs.kotlin.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}