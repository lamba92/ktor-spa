import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project
val kotlinVersion: String by project
val logbackVersion: String by project

plugins {
    maven
    kotlin("jvm") version "1.3.10"
}

group = "it.lamba"
version = "1.0"

repositories {
    mavenLocal()
    jcenter()
    maven { url = uri("https://kotlin.bintray.com/ktor") }
}

dependencies {
    compile("io.ktor:ktor-server-core:$ktorVersion")
    testCompile("io.ktor:ktor-server-tomcat:$ktorVersion")
    testCompile("io.ktor:ktor-server-tests:$ktorVersion")
}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    classifier = "sources"
    from(sourceSets.getAt("main").allSource)
}

artifacts.add("archives", sourcesJar)
