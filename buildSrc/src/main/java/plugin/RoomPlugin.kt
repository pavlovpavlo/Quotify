import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.PLUGIN_ROOM_VERSION
    get() = "2.6.1"

inline val PluginDependenciesSpec.Room: PluginDependencySpec
    get() = id("androidx.room")