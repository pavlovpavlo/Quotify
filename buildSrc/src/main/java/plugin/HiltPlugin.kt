import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.PLUGIN_HITL_VERSION
    get() = "2.51.1"

inline val PluginDependenciesSpec.Hilt: PluginDependencySpec
    get() = id("com.google.dagger.hilt.android")