pluginManagement {

    includeBuild("build-logic")

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

rootProject.name = "RunCombi_Android"
include(":app")
include(":feature:login")
include(":feature:signup")
include(":core:data:user")
include(":core:domain:user")
include(":core:designsystem")
include(":core:navigation")
include(":core:ui")
include(":core:network")
include(":feature:main")
include(":core:data:common")
include(":core:domain:common")
