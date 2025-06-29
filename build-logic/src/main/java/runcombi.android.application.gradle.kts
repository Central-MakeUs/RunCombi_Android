import com.combo.runcombi.convention.configureHiltAndroid
import com.combo.runcombi.convention.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
