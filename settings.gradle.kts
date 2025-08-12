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
        maven { url = java.net.URI("https://jitpack.io") }
        maven { url = java.net.URI("https://devrepo.kakao.com/nexus/content/groups/public/") }
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
include(":feature:history")
include(":feature:setting")
include(":feature:walk")
include(":core:data:auth")
include(":core:datastore")
include(":core:domain:auth")
include(":core:domain:walk")
include(":core:data:walk")
include(":core:domain:history")
include(":core:data:history")
include(":core:domain:setting")
include(":core:data:setting")
include(":core:analytics")
