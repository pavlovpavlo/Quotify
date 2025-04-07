import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.PLUGIN_KOTLIN_VERSION
    get() = "2.1.0"

inline val PluginDependenciesSpec.KotlinAndroid: PluginDependencySpec
    get() = id("org.jetbrains.kotlin.android")

inline val PluginDependenciesSpec.KotlinKapt: PluginDependencySpec
    get() = id("org.jetbrains.kotlin.kapt")

inline val PluginDependenciesSpec.Compose: PluginDependencySpec
    get() = id("org.jetbrains.kotlin.plugin.compose")

inline val PluginDependenciesSpec.KotlinSerialization: PluginDependencySpec
    get() = id("org.jetbrains.kotlin.plugin.serialization")

inline val PluginDependenciesSpec.KotlinParcelize: PluginDependencySpec
    get() = id("kotlin-parcelize")