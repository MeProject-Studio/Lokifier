package ru.meproject.lokifier;

import lombok.Getter;
import lombok.experimental.Accessors;
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

@Accessors(fluent = true)
public class LokifierConfig {
    private final YamlConfigurationLoader loader;
    private final CommentedConfigurationNode node;
    private @Getter Configuration configData;

    public LokifierConfig(Path configPath) throws ConfigurateException {
        // Load from disk
        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        // Make a node
        this.node = loader.load();
        // Serialize to object
        this.configData = node.get(Configuration.class);
        if (!configPath.toFile().exists()) {
            saveConfig();
        }
    }

    public void saveConfig() {
        // Deserialize to node and save on disk
        try {
            node.set(Configuration.class, configData);
            loader.save(node);
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
    }

    @Getter
    @Accessors(fluent = true)
    @ConfigSerializable
    public static class Configuration {
        @Comment("Loki instance URL")
        private String lokiUrl = "https://example.com:3100/";

        @Comment("How often we should push to Loki. Default is 5 seconds")
        private int pushInterval = 5;

        @Comment("Determines amount of log entries to push at once. 0 would mean there is no limit")
        private int maxPushSize = 0;

        @Comment("Labels to put on stream produced by server")
        private @Nullable Map<String, String> labels = Map.of("server", LokifierBukkitPlugin.INSTANCE.getName());
    }
}
