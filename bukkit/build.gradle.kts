plugins {
    id("net.minecrell.plugin-yml.bukkit") version "0.5.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")
    implementation(project(":common"))
}

bukkit {
    name = "Lokifier"
    version = project.version.toString()
    description = "Loki observability"
    website = "https://github.com/MeProjectStudio/Lokifier"

    // Plugin main class (required)
    main = "ru.meproject.lokifier.bukkit.LokifierBukkitPlugin"

    // API version (should be set for 1.13+)
    apiVersion = "1.16"

    // Other possible properties from plugin.yml (optional)
    load = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.PluginLoadOrder.STARTUP
    authors = listOf("rkkm")
    prefix = "lokifier \uD83C\uDF00"
    defaultPermission = net.minecrell.pluginyml.bukkit.BukkitPluginDescription.Permission.Default.OP
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }
    shadowJar {
        fun relocate(pkg: String) = relocate(pkg, "ru.meproject.lokifier.relocated.$pkg")
        relocate("org.yaml.snakeyaml")
    }
}