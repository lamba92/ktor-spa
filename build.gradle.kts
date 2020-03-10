@file:Suppress("UNUSED_VARIABLE")

import com.jfrog.bintray.gradle.BintrayExtension
import com.jfrog.bintray.gradle.tasks.BintrayUploadTask

plugins {
    kotlin("multiplatform")
    `maven-publish`
    id("com.jfrog.bintray")
}

group = "com.github.lamba92"
version = `travis-tag` ?: "1.0"

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/ktor")
    maven(url = "https://jitpack.io")
}

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }
    }

    sourceSets {

        val ktorVersion: String by project
        val resourceLoaderVersion: String by project

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-server-core:$ktorVersion")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation("io.ktor:ktor-server-tests:$ktorVersion")
                implementation("com.github.lamba92:kresourceloader:$resourceLoaderVersion")
            }
        }
    }
}

bintray {
    user = searchPropertyOrNull("bintrayUsername", "BINTRAY_USERNAME")
    key = searchPropertyOrNull("bintrayApiKey", "BINTRAY_API_KEY")
    pkg {
        version {
            name = project.version as String
        }
        repo = "com.github.lamba92"
        name = "ktor-spa"
        setLicenses("Apache-2.0")
        vcsUrl = "https://github.com/lamba92/ktor-spa"
        issueTrackerUrl = "https://github.com/lamba92/ktor-spa/issues"
    }
    publish = true
    setPublications(*publishing.publications.names.toTypedArray())
}

tasks.withType<BintrayUploadTask> {
    doFirst {
        publishing.publications.withType<MavenPublication> {
            buildDir.resolve("publications/$name/module.json").let {
                if (it.exists())
                    artifact(object : org.gradle.api.publish.maven.internal.artifact.FileBasedMavenArtifact(it) {
                        override fun getDefaultExtension() = "module"
                    })
            }
        }
    }
}

fun BintrayExtension.pkg(action: BintrayExtension.PackageConfig.() -> Unit) {
    pkg(closureOf(action))
}

fun BintrayExtension.PackageConfig.version(action: BintrayExtension.VersionConfig.() -> Unit) {
    version(closureOf(action))
}

fun searchPropertyOrNull(name: String, vararg aliases: String): String? {

    fun searchEverywhere(name: String): String? =
        findProperty(name) as? String? ?: System.getenv(name)

    searchEverywhere(name)?.let { return it }

    with(aliases.iterator()) {
        while (hasNext()) {
            searchEverywhere(next())?.let { return it }
        }
    }

    return null
}

@Suppress("PropertyName")
val `travis-tag`
    get() = System.getenv("TRAVIS_TAG").run {
        if (isNullOrBlank()) null else this
    }
