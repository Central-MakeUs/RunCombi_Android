import com.combo.runcombi.convention.setNamespace

plugins {
    id("runcombi.android.feature")
}

android {
    setNamespace("feature.walk")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.maps.compose)
    implementation(libs.play.services.location)
    implementation(libs.accompanist.permissions)

    implementation(libs.lottie.compose)

    implementation(libs.androidx.graphics.shapes)
    implementation(libs.gson)
    
    // Core 모듈 의존성
    implementation(project(":core:data:common"))
    implementation(project(":core:domain:walk"))
    implementation(project(":core:data:walk"))
}