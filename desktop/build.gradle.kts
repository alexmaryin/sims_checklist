import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val decomposeVersion = extra["decompose.version"] as String
val koinVersion= extra["koin.version"] as String

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = extra["app.group"] as String
version = extra["app.version"] as String
val linuxVersion = extra["app.linux.version"] as String
val release = extra["app.release"] as String

kotlin {
    jvm {
        withJava()
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(project(":common"))
                // Decompose navigation library
                implementation("com.arkivanov.decompose:decompose:$decomposeVersion")
                implementation("com.arkivanov.decompose:extensions-compose-jetbrains:$decomposeVersion")
                // Koin-DI
                implementation("io.insert-koin:koin-core:$koinVersion")
                // Realm
                compileOnly("io.realm.kotlin:library-base:1.10.2")
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
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Sims checklists"
            packageVersion = version.toString()
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
                packageVersion = linuxVersion
                debMaintainer = "java.ul@gmail.com"
                menuGroup = "Sims checklists"
                appRelease = release
                iconFile.set(project.file("sims_checklist.ico"))
            }
        }
    }
}