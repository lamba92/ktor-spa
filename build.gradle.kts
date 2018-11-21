import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    application
    kotlin("jvm") version "1.3.10"
}

group = "ktor-spa"
version = "0.0.1"

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    compile(kotlin("stdlib"))
    compile("io.ktor:ktor-server-tomcat:$ktorVersion")
    compile("ch.qos.logback:logback-classic:$logbackVersion")
    testCompile("io.ktor:ktor-server-tests:$ktorVersion")
}

