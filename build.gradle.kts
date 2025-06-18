// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    //Room Ksp 추가 (코틀린 버전 kotlin = "2.0.21")
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
    //끝

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
}