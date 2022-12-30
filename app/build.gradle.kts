import org.jetbrains.kotlin.base.kapt3.KaptOptions
import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}


android {
    namespace = "com.andela.currency"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.andela.currency"
        minSdk = 21
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField ("String", "API_BASE_URL", "\"https://api.apilayer.com/fixer/\"")
        buildConfigField ("String", "API_KEY", "\"FgU8OZUORbMrAOKc5Oac5XrcP9tN7PIG\"")
    }

    buildTypes {
        release {
            //minifyEnabled= false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    val nav_version = "2.5.3"

    // Java language implementation
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    // Kotlin
    implementation("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Feature module Support
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    //network request
    val retrofit_version ="2.9.0"
    val okhttp_logging_version = "4.9.0"
    implementation ("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation ("com.squareup.okhttp3:logging-interceptor:$okhttp_logging_version")

    //hilt di
    val dagger_hilt_version = "2.44"
    val dagger_hilt_viewmodel_version = "1.0.0-alpha03"
    implementation ("com.google.dagger:hilt-android:$dagger_hilt_version")
    kapt("com.google.dagger:hilt-android-compiler:$dagger_hilt_version")

    // Testing
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
    testImplementation("com.google.truth:truth:1.1.3")
    testImplementation("com.google.dagger:hilt-android-testing:2.44")
    kaptTest("com.google.dagger:hilt-android-compiler:2.44")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}