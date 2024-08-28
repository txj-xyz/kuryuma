package config

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import java.io.File


/*
    Class for loading up the settings of the Discord Bot itself
    all settings are loaded in json format
 */
class Settings(
    private val file: String
) {
    private var loaded: Boolean = false
    private val configFile = File(file)
    private val serializer = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    // Check to make sure the file exists when we call the Settings class itself on construction
    init {
        try {
            if(!configFile.exists()) throw NoSuchFileException(
                file = configFile,
                reason = "Config file '$file' does not exist"
            )
        } catch (e: NoSuchFileException) {
            println("$e.localizedMessage")
        }
    }

    // Print out the settings and their stats of the state
    fun printSettings() {
        println("""
              File Name: '$file'
                 Loaded: '$loaded'
        ----------------------------
        """.trimIndent())
    }

    /**
     * Loads up the config file provided in the Class, if there is no config it will generate one
     * @return ConfigData
     */
    suspend fun load(): ConfigData {
        return withContext(Dispatchers.IO) {
            val content = configFile.readText()
            try {
                val configData = serializer.decodeFromString<ConfigData>(content)
                save(configData)
                loaded = true
                configData
            } catch (e: SerializationException) {
                println("Failed to load config, there was an error serializing the data, generating default config")
                val defaultConfig = ConfigData()
                save(defaultConfig)
                loaded = true
                defaultConfig
            }
        }
    }

    /**
     * Saves the config file to disk when prompted
     * @param[config] Kotlin data class representing the save data in Serializable format
     * @return Boolean
     */
    private fun save(config: ConfigData): Boolean {
        return try {
            configFile.writeText(serializer.encodeToString<ConfigData>(config))
            true
        } catch (e: SerializationException) {
            println("Failed to save config due to serialize error: ${e.localizedMessage}")
            false
        }
    }

    /**
     * Pretty prints out the JSON in String form.
     * This will not run if the file has not been loaded first
     *
     * @return [String?]
     */
    fun toJson(): String? {
        if (!loaded) return null
        return try {
            val content = configFile.readText()
            val deserialized = serializer.decodeFromString<ConfigData>(content)
            val encoded = serializer.encodeToString<ConfigData>(deserialized)
            encoded
        } catch (e: SerializationException) {
            println(e.localizedMessage)
            null
        }
    }
}