import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

inline val PluginDependenciesSpec.PLUGIN_KTLINT_VERSION
    get() = "12.1.0"

inline val PluginDependenciesSpec.Ktlint: PluginDependencySpec
    get() = id("org.jlleitschuh.gradle.ktlint")