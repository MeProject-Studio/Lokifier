package ru.meproject.lokifier;

import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;
import ru.meproject.lokifier.console.LokifierConsoleHandler;
import ru.meproject.lokifier.http.LokiDispatcherService;

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
        dispatcherService = new LokiDispatcherService(pluginConfig);
        try {
            getLogger().addHandler(new LokifierConsoleHandler(dispatcherService));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getServer().getScheduler()
                .runTaskTimerAsynchronously(this,
                        new LokifierPushTask(dispatcherService),
                        0,
                        pluginConfig.configData().pushInterval() * 20L);
        INSTANCE = this;
    }

    @Override
    public void onDisable() {

    }
}
