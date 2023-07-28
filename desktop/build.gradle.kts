import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val decomposeVersion = "2.0.1"
val koinVersion= "3.1.4"

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "ru.alexmaryin.simschecklist"
version = "1.0.3"

kotlin {
    jvm {
        jvmToolchain(11)
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
            packageVersion = "1.2.0"
            copyright = "© 2023 Alex Maryin. All rights reserved."
            modules("java.instrument", "java.management", "java.naming", "java.sql", "jdk.unsupported")
            appResourcesRootDir.set(project.layout.projectDirectory.dir("../common/resources"))
            windows {
                console = true
                vendor = "Alex Maryin"
                dirChooser = true
                perUserInstall = true
                menuGroup = "Sims checklists"
                iconFile.set(project.file("sims_checklist.ico"))
            }

            linux {
                // a version for all Linux distributable
                packageVersion = "1:0.3"
                debMaintainer = "java.ul@gmail.com"
                menuGroup = "Sims checklists"
                appRelease = "4"
                iconFile.set(project.file("sims_checklist.ico"))
            }
        }
    }
}