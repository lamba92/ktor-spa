pluginManagement {
    resolutionStrategy {

        val kotlinVersion: String by settings
        val bintrayPublishPlugin: String by settings

        eachPlugin {
            when (requested.id.id) {
                "org.jetbrains.kotlin.jvm", "org.jetbrains.kotlin.multiplatform" ->
                    useVersion(kotlinVersion)
                "com.jfrog.bintray" ->
                    useVersion(bintrayPublishPlugin)
            }
        }
    }
}

plugins {
    `gradle-enterprise`
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(!System.getenv("CI").isNullOrEmpty())
    }
}

rootProject.name = "ktor-spa"