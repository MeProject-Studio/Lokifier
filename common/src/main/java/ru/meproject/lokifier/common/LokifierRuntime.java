package ru.meproject.lokifier.common;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import ru.meproject.lokifier.common.console.LokifierConsoleHandler;
import ru.meproject.lokifier.common.http.LokiDispatcherService;

import java.nio.file.Path;
import java.util.function.Consumer;

@Getter
@Accessors(fluent = true)
public class LokifierRuntime {
    private final LokifierConfig pluginConfig;
    private final LokiDispatcherService dispatcherService;

    public LokifierRuntime(Path configPath, Logger logger, Consumer<LokifierRuntime> localPushTask) {
        this.pluginConfig = new LokifierConfig(configPath);
        this.dispatcherService = new LokiDispatcherService(pluginConfig, logger);
        try {
            java.util.logging.Logger.getLogger("").addHandler(new LokifierConsoleHandler(dispatcherService));
        } catch (Exception e) {
            e.printStackTrace();
        }
        localPushTask.accept(this);
    }
}
