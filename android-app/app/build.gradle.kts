plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.mozilla.rust-android-gradle.rust-android")
    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.apptcheck.agent"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.apptcheck.agent"
        minSdk = 26 
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

/**
 * FIX: High-Fidelity Rust Configuration for Gradle 9.x
 * We use dynamic lookup by name to bypass the 'module' keyword collision
 * and the package resolution issues seen in previous builds.
 */
val rustExtension = extensions.findByName("rust")
if (rustExtension != null) {
    // We use the dynamic 'with' or 'apply' on the object to ensure the 
    // properties are set on the correct receiver.
    with(rustExtension) {
        // Use reflective access via 'javaClass' to ensure we are setting 
        // the properties on the RustExtension object even if the 
        // Kotlin compiler can't see the type clearly.
        javaClass.getMethod("setModule", String::class.java).invoke(this, "../../rust-engine")
        javaClass.getMethod("setLibname", String::class.java).invoke(this, "rust_engine")
        javaClass.getMethod("setTargets", List::class.java).invoke(this, listOf("arm", "arm64", "x86", "x86_64"))
    }
} else {
    throw GradleException("Rust-Android plugin was not initialized correctly.")
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2023.10.01")
    implementation(composeBom)
    
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("net.java.dev.jna:jna:5.13.0@aar")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
}

// Hook the Rust build into the Android compilation lifecycle
tasks.whenTaskAdded {
    if (name == "javaPreCompileDebug" || name == "javaPreCompileRelease") {
        dependsOn("cargoBuild")
    }
}
