package com.combo.runcombi.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

internal fun Project.configureComposeAndroid() {
    with(plugins) {
        apply("org.jetbrains.kotlin.plugin.compose")
    }

    val libs = extensions.libs
    androidExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            implementation(platform(bom))
            implementation(libs.findLibrary("androidx.ui").get())
            implementation(libs.findLibrary("androidx.ui.graphics").get())
            implementation(libs.findLibrary("androidx.ui.tooling.preview").get())
            implementation(libs.findLibrary("androidx.material3").get())
            implementation(libs.findLibrary("androidx.material").get())
            implementation(libs.findLibrary("androidx.navigation.compose").get())
            implementation(libs.findLibrary("androidx.hilt.navigation.compose").get())

            debugImplementation(libs.findLibrary("androidx.ui.tooling").get())
            debugImplementation(libs.findLibrary("androidx.ui.test.manifest").get())

            androidTestImplementation(platform(bom))
            androidTestImplementation(libs.findLibrary("androidx.ui.test.junit4").get())
        }
    }

    extensions.getByType<ComposeCompilerGradlePluginExtension>().apply {
        enableStrongSkippingMode.set(true)
    }
}