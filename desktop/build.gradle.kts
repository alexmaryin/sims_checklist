import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val decomposeVersion = "0.4.0"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":common"))
                // Decompose navigation library
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Sims checklists"
            packageVersion = "1.0.1"
            copyright = "© 2021 Alex Maryin. All rights reserved."

            windows {
                dirChooser = true
                perUserInstall = true
                menuGroup = "Sims checklists"
                iconFile.set(project.file("sims_checklist.ico"))
            }

            linux {
                // a version for all Linux distributable
                packageVersion = "1:0.1"
                debMaintainer = "java.ul@gmail.com"
                menuGroup = "Sims checklists"
                appRelease = "1"
                iconFile.set(project.file("sims_checklist.ico"))
            }
        }
    }
}
