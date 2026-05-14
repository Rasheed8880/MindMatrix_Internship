plugins {
    id("com.android.application") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.21" apply false
    id("com.google.devtools.ksp") version "2.0.21-1.0.25" apply false
    id("com.google.dagger.hilt.android") version "2.52" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

allprojects {
    tasks.matching { it.name == "prepareKotlinBuildScriptModel" }.configureEach {
        group = "help"
        description = "Compatibility task for Android Studio Kotlin DSL model import."
    }

    if (tasks.findByName("prepareKotlinBuildScriptModel") == null) {
        tasks.register("prepareKotlinBuildScriptModel") {
            group = "help"
            description = "Compatibility task for Android Studio Kotlin DSL model import."
        }
    }
}
