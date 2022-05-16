package ru.meproject.lokifier;

import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;

import java.net.MalformedURLException;
import java.net.URL;

public class LokifierBukkitPlugin extends JavaPlugin {
    private LokifierConfig pluginConfig;
    private LokiDispatcherService dispatcherService;
    public static LokifierBukkitPlugin INSTANCE;

    @Override
    public void onEnable() {
        try {
            this.pluginConfig = new LokifierConfig(getDataFolder().toPath().resolve("config.yml"));
        } catch (ConfigurateException e) {
            e.printStackTrace();
        }
        try {
            dispatcherService = new LokiDispatcherService(new URL(pluginConfig.config().lokiUrl), pluginConfig.config().pushInterval);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            getLogger().addHandler(new LokifierConsoleHandler(dispatcherService));
        } catch (Exception e) {
            e.printStackTrace();
        }
        INSTANCE = this;
    }

    @Override
    public void onDisable() {

    }
}
