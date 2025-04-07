
import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.PLUGIN_CRASHLYTICS_VERSION
    get() = "2.9.9"

inline val PluginDependenciesSpec.Crashlytics: PluginDependencySpec
    get() = id("com.google.firebase.crashlytics")
