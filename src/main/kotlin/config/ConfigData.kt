package config

import kotlinx.serialization.*

@Serializable
data class ConfigData(
    val name: String = "default-bot-name",
    val token: String = "default-bot-token",
    val information: BotInformation = BotInformation(),
    val modules: List<Modules> = listOf()
)

@Serializable
data class BotInformation(
    val statusMessage: String = "default-bot-status-message",
)

@Serializable
data class Modules(
    val enabled: List<String> = listOf(),
    val disabled: List<String> = listOf()
)