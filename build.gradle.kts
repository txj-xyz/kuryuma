plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
}

group = "org.txj"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    // Kord Snapshot Repositories (optional, choose one of these):
    maven("https://repo.kord.dev/snapshots")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("dev.kord:kord-core:0.14.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.1")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}