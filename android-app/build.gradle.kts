plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("org.mozilla.rust-android-gradle.rust-android") version "0.9.3" apply false
}

tasks.register<Delete>("clean") {
    delete(layout.buildDirectory)
}
