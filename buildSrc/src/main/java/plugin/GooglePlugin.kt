import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.PLUGIN_GOOGLE_SERVICES_VERSION
    get() = "4.4.0"

inline val PluginDependenciesSpec.GoogleServices: PluginDependencySpec
    get() = id("com.google.gms.google-services")
