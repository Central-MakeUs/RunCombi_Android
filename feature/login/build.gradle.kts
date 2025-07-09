import com.combo.runcombi.convention.setNamespace

plugins {
    id("runcombi.android.feature")
}

android {
    setNamespace("feature.login")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.v2.user)
}