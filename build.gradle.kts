import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.hot.reload)
}

repositories {
    mavenCentral()
    google()
    maven("https://packages.jetbrains.team/maven/p/kpm/public/")
    maven("https://cache-redirector.jetbrains.com/intellij-repository/releases")
    maven("https://cache-redirector.jetbrains.com/intellij-dependencies")
}

dependencies {
    // Compose
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.desktop)
    implementation(libs.compose.resources)
    implementation(libs.compose.ui.tooling)
    
    // Coroutines
    implementation(libs.kotlin.coroutines.swing)
    
    // Serialization
    implementation(libs.kotlinx.serialization)
    
    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    
    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    
    // UI Components
    implementation(libs.lucide.icons)
    implementation(libs.jewel.ui)
    implementation(libs.jewel.window)
    implementation(libs.intellij.icons)
    
    // Logging
    implementation(libs.slf4j.api)
    implementation(libs.slf4j.simple)
    implementation(libs.jna)
    implementation(libs.jna.platform)
    
    // Testing
    testImplementation(libs.kotlin.test)
}

kotlin {
    jvmToolchain(21)

    sourceSets.all {
        languageSettings {
            languageVersion = "2.0"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

group = "com.eltonkola.desktop"
version = "1.0-SNAPSHOT"

compose.desktop {
    application {
        mainClass = "com.eltonkola.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.eltonkola.desktop"
            packageVersion = "1.0.0"
            description = "Compose Dekstop App"
            copyright = "© 2026 Elton Kola. All rights reserved."
            vendor = "Example vendor"
            licenseFile.set(project.file("LICENSE"))
            macOS {
                iconFile.set(project.file("icons/icon.icns"))
            }
            windows {
                iconFile.set(project.file("icons/icon.ico"))
            }
            linux {
                iconFile.set(project.file("icons/icon.png"))
            }
        }
    }
}
