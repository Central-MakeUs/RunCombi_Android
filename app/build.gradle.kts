plugins {
    id("runcombi.android.application")
}

android {
    namespace = "com.combo.runcombi"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.combo.runcombi"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(project(":feature:main"))
    implementation(project(":feature:login"))

    implementation(project(":core:designsystem"))

    implementation(project(":core:data:common"))
    implementation(project(":core:data:user"))
}