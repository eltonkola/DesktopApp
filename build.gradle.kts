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
    // Compose Desktop
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.desktop)
    implementation(libs.compose.resources)
    implementation(libs.compose.ui.tooling)

    // Kotlin Coroutines
    implementation(libs.kotlin.coroutines.swing)

    // Serialization
    implementation(libs.kotlinx.serialization)

    // AndroidX Lifecycle
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    // Ktor Client
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    // UI Components
    implementation(libs.lucide.icons)
    implementation(libs.jewel.ui)
    implementation(libs.jewel.window)
    implementation(libs.intellij.icons)

    // JNA (Native bindings)
    implementation(libs.jna)
    implementation(libs.jna.platform)

    // Logging
    implementation(libs.slf4j.api)
    runtimeOnly(libs.slf4j.simple)

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
val appDisplayName = "Compose Desktop App"
val appMenuGroup = "Compose Apps"

compose.desktop {
    application {
        mainClass = "com.eltonkola.desktop.MainKt"

        // JVM arguments for all platforms
        jvmArgs += listOf(
            "-Dfile.encoding=UTF-8",
            "-Xmx512m", // Max heap size
            "-Xms128m", // Initial heap size
            "-XX:+UseG1GC", // Use G1 garbage collector for better performance
            "-XX:MaxGCPauseMillis=200" // Target max GC pause time
        )

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appDisplayName
            packageVersion = "1.0.0"
            description = appDisplayName
            copyright = "© 2026 Elton Kola. All rights reserved."
            vendor = "Elton Kola"
            licenseFile.set(project.file("LICENSE"))

            macOS {
                iconFile.set(project.file("icons/icon.icns"))
                bundleID = "com.eltonkola.desktop"
                // Require macOS 11.0 or later for better compatibility
                minimumSystemVersion = "11.0"

                infoPlist {
                    extra["CFBundleDisplayName"] = appDisplayName
                    extra["CFBundleName"] = appDisplayName
                    extra["CFBundleGetInfoString"] = appDisplayName
                    extra["LSMinimumSystemVersion"] = "11.0"
                    extra["NSHighResolutionCapable"] = true
                }

                // macOS-specific JVM args
                jvmArgs += listOf(
                    "-Dapple.awt.application.appearance=system", // Follow system theme
                    "-Dapple.awt.application.name=$appDisplayName"
                )
            }

            windows {
                iconFile.set(project.file("icons/icon.ico"))
                menuGroup = appMenuGroup
                upgradeUuid = "33FED4E9-7E0D-44D9-8DB5-12AC69AEADB6"
                msiPackageVersion = "1.0.0"
                perUserInstall = true
                dirChooser = true // Allow user to choose installation directory

                // Windows-specific JVM args
                jvmArgs += listOf(
                    "-Dsun.java2d.d3d=false", // Fix graphics issues on some systems
                    "-Dsun.java2d.noddraw=true" // Disable DirectDraw
                )
            }

            linux {
                iconFile.set(project.file("icons/icon.png"))
                menuGroup = appMenuGroup
                appRelease = "1.0.0"
                appCategory = "Utility"

                // Linux-specific JVM args
                jvmArgs += listOf(
                    "-Dsun.java2d.opengl=true" // Better Linux graphics performance
                )
            }

            appResourcesRootDir.set(project.layout.projectDirectory.dir("resources"))

            modules(
                "jdk.accessibility",
                "java.logging",
                "jdk.crypto.ec",
                "java.desktop",
                "java.net.http",
                "jdk.unsupported",
                "java.datatransfer",
                "java.prefs",
                "java.xml",
                "java.base"
            )
        }
    }
}