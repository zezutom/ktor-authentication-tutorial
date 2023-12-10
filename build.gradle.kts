import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "1.9.21"
    id("io.ktor.plugin") version "2.3.7" apply false
}

group = "com.tomaszezula.ktorauth"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "io.ktor.plugin")

    configure<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension> {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }
    dependencies {
        implementation("io.ktor:ktor-server-core-jvm")
        implementation("io.ktor:ktor-server-auth-jvm")
        implementation("io.ktor:ktor-server-netty-jvm")
        implementation("ch.qos.logback:logback-classic:$logback_version")
        testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
        testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")
    }
}


