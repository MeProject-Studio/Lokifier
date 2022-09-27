package ru.meproject.lokifier.common;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.net.URL;
import java.nio.file.Path;
import java.util.Map;

@Accessors(fluent = true)
public class LokifierConfig {
    private final YamlConfigurationLoader loader;
    private final CommentedConfigurationNode node;
    private @Getter Configuration configData;

    public LokifierConfig(Path configPath) {
        // Load from disk
        this.loader = YamlConfigurationLoader.builder()
                .path(configPath)
                .nodeStyle(NodeStyle.BLOCK)
                .build();
        // Make a node
        try {
            this.node = loader.load();
            // Serialize to object
            this.configData = node.get(Configuration.class);
            if (!configPath.toFile().exists()) {
                saveConfig();
            }
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
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
        private URL lokiUrl = Utils.getURL("http://localhost:3100");

        @Comment("How often we should push to Loki. Default is 30 seconds")
        private int pushInterval = 10;

        @Comment("HTTP Basic Auth")
        private BasicAuthSection basicAuth;

        @Comment("Labels to put on stream produced by server")
        private Map<String, String> labels = Map.of("job", "MyServer");
    }

    @Getter
    @Accessors(fluent = true)
    @ConfigSerializable
    public static class BasicAuthSection {
        private boolean enable = false;
        private String username = "user";
        private String password = "password" ;
    }
}
