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
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.desktop)
    implementation(libs.compose.resources)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.kotlin.coroutines.swing)

    implementation(libs.kotlinx.serialization)
    implementation(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.lifecycle.runtimeCompose)


    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-cio:2.3.7") 
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    
    implementation("com.composables:icons-lucide-cmp:+")

    val jewelVersion = "0.33.0-253.29795"
    implementation("org.jetbrains.jewel:jewel-int-ui-standalone:$jewelVersion")
    implementation("org.jetbrains.jewel:jewel-int-ui-decorated-window:$jewelVersion")

    implementation("com.jetbrains.intellij.platform:icons:253.29346.145")
    
    

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
