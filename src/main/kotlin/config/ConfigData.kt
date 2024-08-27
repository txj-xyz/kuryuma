package config

import kotlinx.serialization.*
//import kotlinx.serialization.json.*

@Serializable
data class ConfigData(
    val name: String,
    val token: String,
    val database: String,
)
