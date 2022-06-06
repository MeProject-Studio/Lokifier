package ru.meproject.lokifier.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import okhttp3.*;
import ru.meproject.lokifier.Utils;
import ru.meproject.lokifier.console.LokiLogEntry;
import ru.meproject.lokifier.LokifierBukkitPlugin;
import ru.meproject.lokifier.LokifierConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class LokiDispatcherService {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private final LinkedBlockingQueue<LokiLogEntry> pushQueue;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final LokifierConfig lokifierConfig;
    private AtomicBoolean lokiReadiness = new AtomicBoolean(false);

    public LokiDispatcherService(LokifierConfig config) {
        this.lokifierConfig = config;
         this.client =  new OkHttpClient.Builder()
                 .authenticator(new LokiBasicAuthenticator(config))
                 .build();
         this.pushQueue = new LinkedBlockingQueue<>();
         this.objectMapper = new ObjectMapper();

    }

    public void addRecord(long unixTime, String logLine) {
        this.pushQueue.add(new LokiLogEntry(unixTime, logLine));
    }

    public ArrayList<LokiLogEntry> pollLastEntries() {
        ArrayList<LokiLogEntry> array = new ArrayList<>();
        int drainSize = Integer.MAX_VALUE;
        if (lokifierConfig.configData().maxPushSize() > 0) {
            drainSize = lokifierConfig.configData().maxPushSize();
        }
        this.pushQueue.drainTo(array, drainSize);
        return array;
    }

    public void pushToLoki() throws IOException {
        var lokiStreamData = LokiPushRequest.LokiStreamData.builder()
                .stream(lokifierConfig.configData().labels());

        List<String[]> values = new ArrayList<>();
        pollLastEntries().stream().map(LokiLogEntry::asArray).forEachOrdered(values::add);
        lokiStreamData.values(values);

        final var lokiPushRequest = new LokiPushRequest(ImmutableList.of(lokiStreamData.build()));

        final var lokiPushEndpoint = new HttpUrl.Builder()
                .scheme(lokifierConfig.configData().lokiUrl().getProtocol())
                .host(lokifierConfig.configData().lokiUrl().getHost())
                .port(lokifierConfig.configData().lokiUrl().getPort())
                .encodedPath("/loki/api/v1/push")
                .build();

        final var request = new Request.Builder()
                .url(lokiPushEndpoint)
                .post(RequestBody.create(objectMapper.writeValueAsString(lokiPushRequest), MEDIA_TYPE_JSON))
                .build();

        try (var response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Utils.logger().info(String.format("Sent %d logs to Loki!", values.size()));
            }
        }
    }

    /*public void readinessProbe() throws IOException {
        final var lokiProbeEndpoint = new HttpUrl.Builder()
                .scheme(lokifierConfig.configData().lokiUrl().getProtocol())
                .host(lokifierConfig.configData().lokiUrl().getHost())
                .port(lokifierConfig.configData().lokiUrl().getPort())
                .encodedPath("/ready");

        final var request = new Request.Builder().url(lokiProbeEndpoint.build()).get().build();
        try (var response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                lokiReadiness.set(true);
            }
        }
    }*/

    /*private void setLokiDown() {
        lokiReadiness.set(false);
        Utils.logger().warning("Is Loki instance down?");
    }

    public boolean isLokiReady() {
        return lokiReadiness.get();
    }*/
}
