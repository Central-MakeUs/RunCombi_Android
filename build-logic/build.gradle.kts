plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "runcombi.android.application"
            implementationClass = "RuncombiAndroidApplicationPlugin"
        }
        register("androidLibrary") {
            id = "runcombi.android.library"
            implementationClass = "RuncombiAndroidLibrary"
        }
        register("androidHilt") {
            id = "runcombi.android.hilt"
            implementationClass = "com.combo.runcombi.convention.HiltAndroidPlugin"
        }
    }
}