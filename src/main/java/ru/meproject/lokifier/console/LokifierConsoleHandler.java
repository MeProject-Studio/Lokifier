package ru.meproject.lokifier.console;

import ru.meproject.lokifier.http.LokiDispatcherService;

import java.util.concurrent.TimeUnit;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

public class LokifierConsoleHandler extends ConsoleHandler {
    private final LokiDispatcherService dispatcherService;

    public LokifierConsoleHandler(LokiDispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public void publish(LogRecord record) {
        // Loki needs unix epoch in nanoseconds
        dispatcherService.addRecord(TimeUnit.NANOSECONDS.convert(record.getMillis(), TimeUnit.MILLISECONDS), getFormatter().format(record));
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
