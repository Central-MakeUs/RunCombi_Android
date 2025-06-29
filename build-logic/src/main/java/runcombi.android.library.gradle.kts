import com.combo.runcombi.convention.configureCoroutineAndroid
import com.combo.runcombi.convention.configureHiltAndroid
import com.combo.runcombi.convention.configureKotlinAndroid

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()
