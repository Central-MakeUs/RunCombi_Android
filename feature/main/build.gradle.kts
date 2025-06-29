import com.combo.runcombi.convention.setNamespace

plugins {
    id("runcombi.android.feature")
}

android {
    setNamespace("feature.main")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.lifecycle)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.bundles.android.test)

    implementation(project(":feature:login"))
    implementation(project(":feature:onboarding"))
}