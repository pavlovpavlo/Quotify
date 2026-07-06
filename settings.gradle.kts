pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Quotify"
include(":app")
include(":domain")
include(":design-systems")
include(":feature:splash")
include(":core:ui")
include(":core:navigation")
include(":core:models")
include(":core:datastore")
include(":core:firebase")
include(":data:auth")
include(":data:settings")
include(":data:library")
include(":feature:onboarding")
include(":feature:auth")
include(":feature:main")
include(":feature:webview")
