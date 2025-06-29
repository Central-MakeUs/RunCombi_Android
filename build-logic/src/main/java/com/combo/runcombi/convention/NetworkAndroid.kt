package com.combo.runcombi.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureNetworkAndroid() {
    val libs = extensions.libs

    dependencies {
        implementation(libs.findLibrary("retrofit").get())
        implementation(libs.findLibrary("retrofit.kotlinx.serialization").get())
        implementation(libs.findLibrary("okhttp3").get())
        implementation(libs.findLibrary("okhttp3.logging.interceptor").get())
    }
}