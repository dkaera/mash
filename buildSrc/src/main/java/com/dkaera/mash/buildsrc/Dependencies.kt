package com.dkaera.mash.buildsrc

object Versions {
    const val ktlint = "0.45.2"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.1"
    const val timber = "com.jakewharton.timber:timber:5.0.1"
    const val googleServices = "com.google.gms:google-services:4.3.12"

    object Kotlin {
        private const val version = "1.6.21"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.6.0"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object JUnit {
        private const val version = "4.13"
        const val junit = "junit:junit:$version"
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.7.0"
        const val navigation = "androidx.navigation:navigation-compose:2.4.2"
        const val lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:2.3.1"

        object Activity {
            const val activityCompose = "androidx.activity:activity-compose:1.4.0"
        }

        object Compose {
            const val snapshot = ""
            const val version = "1.2.0-rc01"

            const val animation = "androidx.compose.animation:animation:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"
            const val iconsExtended = "androidx.compose.material:material-icons-extended:$version"
            const val material = "androidx.compose.material:material:$version"
            const val material3 = "androidx.compose.material3:material3:1.0.0-alpha01"
            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val toolingPreview = "androidx.compose.ui:ui-tooling-preview:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"
            const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:$version"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1"
            const val hilt = "androidx.hilt:hilt-navigation-compose:1.0.0"
        }

        object ConstraintLayout {
            const val constraintLayoutCompose =
                "androidx.constraintlayout:constraintlayout-compose:1.0.0"
        }

        object Google {
            const val bom = "com.google.firebase:firebase-bom:30.2.0"
            const val analytics = "com.google.firebase:firebase-analytics-ktx"
            const val auth = "com.google.firebase:firebase-auth-ktx"
            const val firestore = "com.google.firebase:firebase-firestore-ktx"
        }

        object Test {
            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"

            object Espresso {
                const val core = "androidx.test.espresso:espresso-core:3.4.0"
            }

            object Ext {
                private const val version = "1.1.2"
                const val junit = "androidx.test.ext:junit-ktx:$version"
            }
        }
    }

    object Coil {
        const val coilCompose = "io.coil-kt:coil-compose:2.0.0"
    }
}
