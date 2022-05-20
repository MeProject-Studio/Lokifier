package ru.meproject.lokifier;

import org.bukkit.scheduler.BukkitRunnable;
import ru.meproject.lokifier.http.LokiDispatcherService;

import java.io.IOException;

public class LokifierPushTask implements Runnable {
    private final LokiDispatcherService dispatcherService;

    public LokifierPushTask(LokiDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public void run() {
        try {
            if (!dispatcherService.isLokiReady()) {
                dispatcherService.readinessProbe();
            }
            dispatcherService.pushToLoki();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
