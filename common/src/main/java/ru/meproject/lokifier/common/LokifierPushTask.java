package ru.meproject.lokifier.common;

import ru.meproject.lokifier.common.http.LokiDispatcherService;

import java.io.IOException;

public class LokifierPushTask implements Runnable {
    private final LokiDispatcherService dispatcherService;

    public LokifierPushTask(LokiDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public void run() {
        try {
            dispatcherService.pushToLoki();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
