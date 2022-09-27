import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.meproject.lokifier.common.LokifierPushTask;
import ru.meproject.lokifier.common.LokifierRuntime;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "lokifier", name = "Lokifier", version = "0.1.0-SNAPSHOT",
        url = "https://example.org", description = "Loki observability", authors = {"rkkm"})
public class LokifierVelocityPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;
    private LokifierRuntime runtime;

    @Inject
    public LokifierVelocityPlugin(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.runtime = new LokifierRuntime(dataDirectory.resolve("config.yml"), logger, this::registerPushTask);
    }

    private void registerPushTask(LokifierRuntime runtime) {
        server.getScheduler()
                .buildTask(this, new LokifierPushTask(runtime.dispatcherService()))
                .repeat(runtime.pluginConfig().configData().pushInterval(), TimeUnit.SECONDS)
                .schedule();
    }
}
