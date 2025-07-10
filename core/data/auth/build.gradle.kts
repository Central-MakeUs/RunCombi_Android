plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
}

android {
    namespace = "com.combo.runcombi.data.auth"
    compileSdk = 35

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.bundles.test)
    implementation(libs.bundles.coroutines)
    implementation(libs.retrofit)

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(project(":core:data:common"))
    implementation(project(":core:domain:common"))
    implementation(project(":core:domain:auth"))
    implementation(project(":core:network"))
    implementation(project(":core:datastore"))
    implementation(project(":core:navigation"))
}