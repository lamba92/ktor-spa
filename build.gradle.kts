plugins {
    maven
    kotlin("jvm")
}

group = "it.lamba"
version = "1.0"

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/ktor")
    maven(url = "https://jitpack.io")
}

dependencies {

    val ktorVersion: String by project

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tomcat:$ktorVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("io.ktor:ktor-client-apache:$ktorVersion")
    testImplementation("com.github.lamba92", "kresourceloader", "1.1")

}

val sourcesJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "Assembles sources JAR"
    archiveClassifier.set("sources")
    from(sourceSets.getAt("main").allSource)
}

artifacts.add("archives", sourcesJar)
