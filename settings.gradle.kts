// E:\Flag-Quiz\settings.gradle.kts
pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // This is the default and causes the error if violated
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Flag-Quiz" // Your project name
include(":app") // Include your app module
// If you have other modules, include them here:
// include(":another_module")