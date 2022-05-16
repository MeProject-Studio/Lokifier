package ru.meproject.lokifier;

import okhttp3.OkHttpClient;

import java.net.URL;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public class LokiDispatcherService {

    private LinkedBlockingQueue<LokiLogEntry> pushQueue;
    private final OkHttpClient client;

    public LokiDispatcherService(URL lokiUrl, int pushInterval) {
         client =  new OkHttpClient();
         pushQueue = new LinkedBlockingQueue<>();
    }

    public void addRecord(long unixTime, String logLine) {
        pushQueue.add(new LokiLogEntry(unixTime, logLine));
    }

    public void flushQueue() {
        LinkedList<LokiLogEntry> toPush = new LinkedList<>();
        pushQueue.drainTo(toPush);

    }
}
