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
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.desktop)
    implementation(libs.compose.material3)
    implementation(libs.compose.resources)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.kotlin.coroutines.swing)

    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.compose.ui.tooling)
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
        }
    }
}
