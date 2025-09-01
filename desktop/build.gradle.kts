import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    jvm {}

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":shared"))
                // Decompose navigation library
                implementation(libs.decompose)
                implementation(libs.decompose.extensions)
                // Koin-DI
                implementation(libs.koin)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        buildTypes.release.proguard {
//            isEnabled.set(false)
            configurationFiles.from(project.file("compose-desktop.pro"))
        }

        nativeDistributions {
            targetFormats(TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Sims checklists"
            copyright = "© 2023 Alex Maryin. All rights reserved."
            modules("java.instrument", "java.management", "java.naming", "java.sql", "jdk.unsupported")
            appResourcesRootDir.set(project.layout.projectDirectory.dir("../common/resources"))
            windows {
                console = false
                vendor = "Alex Maryin"
                dirChooser = true
                perUserInstall = true
                menuGroup = "Sims checklists"
                iconFile.set(project.file("sims_checklist.ico"))
            }

            linux {
                // a version for all Linux distributable
                debPackageVersion = "1:4.0"
                debMaintainer = "java.ul@gmail.com"
                menuGroup = "Sims checklists"
                appRelease = libs.versions.app.release.toString()
                iconFile.set(project.file("sims_checklist.ico"))
            }
        }
    }
}