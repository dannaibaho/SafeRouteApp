// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false // BARU
    alias(libs.plugins.kotlin.plugin.compose) apply false // BARU
    id("com.google.gms.google-services") version "4.4.3" apply false // BARU
    id("com.google.firebase.crashlytics") version "3.0.5" apply false // BARU
}

// Task untuk membersihkan direktori build
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}