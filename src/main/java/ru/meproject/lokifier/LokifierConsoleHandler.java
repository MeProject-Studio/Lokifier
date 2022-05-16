package ru.meproject.lokifier;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LokifierConsoleHandler extends Handler {
    private final LokiDispatcherService dispatcherService;

    public LokifierConsoleHandler(LokiDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public void publish(LogRecord record) {
        dispatcherService.addRecord(record.getMillis(), getFormatter().format(record));
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
