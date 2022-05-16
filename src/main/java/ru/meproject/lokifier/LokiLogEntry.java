package ru.meproject.lokifier;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class LokiLogEntry {
    private final long unixTime;
    private final String logLine;

    public LokiLogEntry(final long unixTime, final String logLine) {
        this.unixTime = unixTime;
        this.logLine = logLine;
    }

    public long unixTime() {
        return unixTime;
    }

    public String logLine() {
        return logLine;
    }
}
