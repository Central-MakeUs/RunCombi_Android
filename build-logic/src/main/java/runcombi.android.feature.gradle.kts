import com.combo.runcombi.convention.configureHiltAndroid
import com.combo.runcombi.convention.implementation

plugins {
    id("runcombi.android.library")
    id("runcombi.android.compose")
}

android {
    packaging {
        resources {
            excludes.add("META-INF/**")
        }
    }
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

}

configureHiltAndroid()

dependencies {
    implementation(project(":core:designsystem"))
    implementation(project(":core:navigation"))
    implementation(project(":core:ui"))
    implementation(project(":core:domain:auth"))
    implementation(project(":core:domain:common"))
    implementation(project(":core:domain:user"))
    implementation(project(":core:domain:walk"))
    implementation(project(":core:domain:history"))
}
