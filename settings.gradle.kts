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

rootProject.name = "DevHub"
include(":app")
include(":core:common")
include(":core:network")
include(":core:database")
include(":core:designsystem")
include(":core:model")
include(":feature:feed")
include(":feature:repository-detail")
include(":feature:search")
include(":feature:bookmarks")
