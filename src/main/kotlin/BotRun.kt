import config.Settings

suspend fun main() {
//    val kord = Kord("your bot token")
//    kord.login()
    val config = Settings("config.json")
    config.load()
    config.printSettings()
}