package ru.meproject.lokifier.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import ru.meproject.lokifier.common.LokifierPushTask;
import ru.meproject.lokifier.common.LokifierRuntime;

public class LokifierBukkitPlugin extends JavaPlugin {
    private LokifierRuntime pluginRuntime;
    public static LokifierBukkitPlugin INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;
        pluginRuntime = new LokifierRuntime(getDataFolder().toPath().resolve("config.yml"), getSLF4JLogger(), this::registerPushTask);
    }

    // Purely for functional style
    public void registerPushTask(LokifierRuntime runtime) {
        getServer().getScheduler()
                .runTaskTimerAsynchronously(INSTANCE,
                        new LokifierPushTask(runtime.dispatcherService()),
                        0,
                        runtime.pluginConfig().configData().pushInterval() * 20L);
    }

    @Override
    public void onDisable() {

    }
}
