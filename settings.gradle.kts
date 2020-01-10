pluginManagement {
    resolutionStrategy {
        val kotlinVersion: String by settings
        eachPlugin {
            if (requested.id.namespace!!.startsWith("org.jetbrains.kotlin")) {
                useVersion(kotlinVersion)
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