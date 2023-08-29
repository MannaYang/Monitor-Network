pluginManagement {
    repositories {
        google()
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

rootProject.name = "Monitor-Network"
include(":app")
include(":stone")
include(":http-export")
include(":http")
include(":room")
include(":room-export")
include(":report")
include(":report-export")
