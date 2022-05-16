package ru.meproject.lokifier.console;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class LokiLogEntry {
    private final long unixTime;
    private final String logLine;

    public LokiLogEntry(final long unixTime, final String logLine) {
        this.unixTime = unixTime;
        this.logLine = logLine;
    }

    public String[] asArray() {
        String[] array = new String[2];
        array[0] = Long.toString(unixTime);
        array[1] = logLine;
        return array;
    }
}
