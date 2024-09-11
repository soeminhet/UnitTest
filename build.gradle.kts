// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.devtools.ksp) apply false
    alias(libs.plugins.com.hilt.android) apply false
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization) apply false
    id("de.mannodermaus.android-junit5") version "1.9.3.0" apply false
}