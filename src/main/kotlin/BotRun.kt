import config.Settings
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
//    val kord = Kord("your bot token")
//    kord.login()
    val config = Settings(file = "config.json")
    config.load()
    config.printSettings()
    println("config load: ${config.toJson()}")
}

