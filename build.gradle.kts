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
val appDisplayName = "Compose Dekstop App"
val appMenuGroup = "Compose Apps"

compose.desktop {
    application {
        mainClass = "com.eltonkola.desktop.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.eltonkola.desktop"
            packageVersion = "1.0.0"
            description = appDisplayName
            copyright = "© 2026 Elton Kola. All rights reserved."
            vendor = "Example vendor"
            licenseFile.set(project.file("LICENSE"))
            macOS {
                iconFile.set(project.file("icons/icon.icns"))
                bundleID = "com.eltonkola.desktop"
                infoPlist {
                    extra["CFBundleDisplayName"] = appDisplayName
                    extra["CFBundleName"] = appDisplayName
                    extra["CFBundleGetInfoString"] = appDisplayName
                }
            }
            windows {
                iconFile.set(project.file("icons/icon.ico"))
                // Windows display name settings
                menuGroup = appMenuGroup  // Start menu folder
                // The .exe filename comes from packageName above
                upgradeUuid = "33FED4E9-7E0D-44D9-8DB5-12AC69AEADB6"  // Generate with: uuidgen
                msiPackageVersion = "1.0.0"
                perUserInstall = true

                // Windows-specific JVM args
                jvmArgs.add("-Dfile.encoding=UTF-8")
                jvmArgs.add("-Dsun.java2d.d3d=false")  // Fix graphics issues on some systems
            }
            linux {
                iconFile.set(project.file("icons/icon.png"))
                // Linux display name settings
                menuGroup = appMenuGroup  // Application menu category
                // The binary name comes from packageName above

                // Optional: Create desktop entry with proper name
                appRelease = "1.0.0"
                appCategory = "Utility"

                // Linux-specific JVM args
                jvmArgs.add("-Dfile.encoding=UTF-8")
                jvmArgs.add("-Dsun.java2d.opengl=true")  // Better Linux graphics
            }
            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))
            modules(
                // REQUIRED for all Java apps
                "java.base",

                // REQUIRED for Compose Desktop UI
                "java.desktop",

                // REQUIRED for Ktor HTTP client (ktor-client-cio)
                "java.net.http",
                "jdk.crypto.ec",

                // REQUIRED for Jewel UI (IntelliJ theme)
                "jdk.accessibility",

                // REQUIRED for JNA (native access)
                "jdk.unsupported",
            )

        }
    }
}
