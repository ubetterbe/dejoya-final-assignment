plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dejoyafinalassessment"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.dejoyafinalassessment"
        minSdk = 27
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        // using XML layouts for this project, viewBinding gives us typed view refs
        // without writing findViewById everywhere
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // ViewModel + LiveData for LoginViewModel's UI state (loading/success/error)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)

    // Navigation Component - MainActivity hosts a NavHostFragment, Dashboard/Details
    // are Fragments swapped in and out of it rather than separate Activities
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Dashboard's list of sports entities
    implementation(libs.androidx.recyclerview)

    // networking - retrofit talks to the API, gson converter turns json into our data classes
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    // DI - koin wires up our repositories/viewmodels without a ton of manual boilerplate
    implementation(libs.koin.android)

    testImplementation(libs.junit)
    // ViewModel unit tests - MockK fakes ApiService, coroutines-test drives
    // viewModelScope's Dispatchers.Main on the JVM (no Android device needed)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    // LiveData.setValue() asserts it's on the main thread, which doesn't exist in a
    // plain JVM test - this rule fakes that so the ViewModels' _uiState.value = ... works
    testImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
