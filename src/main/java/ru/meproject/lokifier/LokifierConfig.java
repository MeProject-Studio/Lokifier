package ru.meproject.lokifier;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.annotation.Nullable;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class LokifierConfig {
    private YamlConfigurationLoader loader;
    private CommentedConfigurationNode node;
    private Configuration config;

    public LokifierConfig(Path configPath) throws ConfigurateException {
        // Load from disk
        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        // Make a node
        this.node = loader.load();
        // Serialize to object
        this.config = node.get(Configuration.class);
        if (!configPath.toFile().exists()) {
            saveConfig();
        }
    }

    public void saveConfig() {
        // Deserialize to node and save on disk
        try {
            node.set(Configuration.class, config);
            loader.save(node);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    public Configuration config() {
        return config;
    }

    @ConfigSerializable
    static class Configuration {
        @Comment("Loki instance URL")
        public String lokiUrl = "http://example.com:3100";

        @Comment("How often we should push to Loki. Default is 5 seconds")
        public int pushInterval = 5;

        @Comment("Determines amount of log entries to push at once. 0 would mean there is no limit")
        public int maxPushSize = 0;

        /*@Comment("Labels to put on stream produced by server")
        public @Nullable Map<String, String> labels = Map.of("server", LokifierBukkitPlugin.INSTANCE.getName());*/
    }
}
