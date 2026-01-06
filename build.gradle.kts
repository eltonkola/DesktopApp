import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.2.21"
    id("org.jetbrains.compose") version "1.10.0-beta01"
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.20"
    id("org.jetbrains.compose.hot-reload") version "1.0.0-rc02"
}

group = "com.eltonkola.desktop"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.compose.material3:material3:1.9.0-beta03")
    implementation("org.jetbrains.compose.components:components-resources:1.10.0-beta01")
    implementation("org.jetbrains.androidx.lifecycle:lifecycle-runtime-compose:2.9.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.10.2")

    implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0-beta01")
    
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}

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
