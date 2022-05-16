import net.minecrell.pluginyml.bukkit.BukkitPluginDescription.*

plugins {
    java
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "ru.meproject"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.12.2-R0.1-SNAPSHOT")
    implementation("org.spongepowered:configurate-yaml:4.1.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")
    compileOnly("org.projectlombok:lombok:1.18.22")
    annotationProcessor("org.projectlombok:lombok:1.18.22")
}

bukkit {
    name = "Lokifier"
    version = project.version.toString()
    description = "Loki observability"
    website = "https://example.com"

    // Plugin main class (required)
    main = "ru.meproject.lokifier.LokifierBukkitPlugin"

    // API version (should be set for 1.13+)
    apiVersion = "1.12"

    // Other possible properties from plugin.yml (optional)
    load = PluginLoadOrder.STARTUP
    authors = listOf("realkarmakun")
    prefix = "lokifier"
    defaultPermission = Permission.Default.OP
}

tasks {
    runServer {
        minecraftVersion("1.12.2")
    }

    /*shadowJar {
        shadowJar {
            fun relocate(pkg: String) = relocate(pkg, "ru.meproject.lokifier.relocated.$pkg")
            relocate("org.spongepowered.configurate")
        }
    }*/
}