import org.jetbrains.kotlin.gradle.dsl.Coroutines
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion: String by project

plugins {
    maven
    kotlin("jvm")
}

group = "it.lamba"
version = "1.0"

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/ktor")
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
