plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // This plugin connects Gradle to Cargo (Rust's build tool)
    id("org.mozilla.rust-android-gradle.rust-android") version "0.9.3"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.apptcheck.agent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.apptcheck.agent"
        minSdk = 26 // Android 8.0 (Oreo)
        targetSdk = 34
        versionCode = 1
        versionName = "3.2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

// Rust Integration Configuration
rust {
    // Path to the Rust project root relative to this file
    module = "../../rust-engine"
    // The library name defined in rust-engine/Cargo.toml [lib]
    libname = "rust_engine"
    // Targets to build for (covers 99% of Android devices)
    targets = listOf("arm", "arm64", "x86", "x86_64")
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    
    // Core Android & UI
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    
    // Navigation & Serialization
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Concurrency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // UniFFI Runtime (Must match Rust UniFFI version)
    // This provides the necessary JNA/JNI glue for the generated Kotlin code
    implementation("net.java.dev.jna:jna:5.13.0@aar")
}

// Ensure Rust builds before Kotlin compiles, as Kotlin depends on Rust's generated files
tasks.whenTaskAdded {
    if (name == "javaPreCompileDebug" || name == "javaPreCompileRelease") {
        dependsOn("cargoBuild")
    }
}
