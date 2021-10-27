buildscript {
    // __LATEST_COMPOSE_RELEASE_VERSION__
    val composeVersion = "1.0.0-alpha4-build362"

    repositories {
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    dependencies {
        classpath("org.jetbrains.compose:compose-gradle-plugin:$composeVersion")
        classpath("com.android.tools.build:gradle:4.2.2")
        // __KOTLIN_COMPOSE_VERSION__
        classpath(kotlin("gradle-plugin", version = "1.5.31"))
    }
}

plugins {
    id("io.gitlab.arturbosch.detekt").version("1.18.1")
}

detekt {
    toolVersion = "1.18.1"
    config = files("config/detekt/detekt.yml")
    source = files("common/src", "android/src/main/java", "desktop/src/jvmMain")
    buildUponDefaultConfig = true
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
