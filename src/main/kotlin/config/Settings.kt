@file:OptIn(ExperimentalSerializationApi::class)

package config

import com.sun.media.sound.InvalidFormatException
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlin.system.exitProcess
import java.io.File


/*
    Class for loading up the settings of the Discord Bot itself
    all settings are loaded in json format
 */
class Settings(
    private val file: String,
    private val generate: Boolean = false
) {
    private var loaded: Boolean = false

    // Print out the settings and their stats of the state
    fun printSettings() {
        println("""
              File Name: '$file'
                 Loaded: '$loaded'
        Generate Config: '$generate'
        """.trimIndent())
    }

    fun load(): ConfigData {
        // Read config file as UTF8 based and load json
        val config = File(file)
        lateinit var loadedJSON: ConfigData

        // Generate a JSON serializer
        val serializer = Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        }

        // If the config file doesn't exist then crash
        if (!config.exists()) {
            throw NoSuchFileException(
                file = config,
                reason = "$file does not exist, cannot load, exiting."
            )
        }

        // If the config is empty we crash out
        if (config.readText().isEmpty()) {
            val defaultConfig = ConfigData(
                name = "example-bot",
                token = "example-bot-token",
                database = "example-bot-database",
            )
            val jsonDefault = serializer.encodeToString(
                serializer = ConfigData.serializer(),
                value = defaultConfig
            )
            File(file).writeText(jsonDefault) // Write out the default config data
            loaded = true
            println("Config was empty generating: \n----\n${config.readText()}")
            exitProcess(1)
        }

        try {
            val raw: String = config.readText()
            loadedJSON = serializer.decodeFromString<ConfigData>(raw)
        } catch (e: Exception) {
            when(e) {
                is MissingFieldException -> {
                    println("[JSON-ERROR] Missing field(s) in '$file' - '${e.missingFields}'")
                    exitProcess(1)
                }
                is SerializationException -> {
                    println("Error decoding JSON, bad character: ${e.message}")
                    exitProcess(1)
                }
            }
        }
        return loadedJSON // OK return out the JSON
        // note: need to revamp this, possibly use gson
    }
}