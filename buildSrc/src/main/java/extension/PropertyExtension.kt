import org.gradle.api.Project
import org.gradle.kotlin.dsl.KotlinSettingsScript
import java.util.Properties

fun KotlinSettingsScript.getLocalProperties(
    fileProperties: String = "local.properties"
) : Properties {
    val propertiesFile = file(fileProperties)
    val properties = Properties()
    properties.load(propertiesFile.inputStream())
    return properties
}