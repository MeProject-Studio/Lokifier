package ru.meproject.lokifier.common.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import ru.meproject.lokifier.common.LokifierConfig;
import ru.meproject.lokifier.common.console.LokiLogEntry;
import okhttp3.*;

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
    private final Logger logger;
    private AtomicBoolean lokiReadiness = new AtomicBoolean(false);

    public LokiDispatcherService(LokifierConfig config, Logger logger) {
        this.lokifierConfig = config;
         this.client =  new OkHttpClient.Builder()
                 .authenticator(new LokiBasicAuthenticator(config))
                 .build();
         this.pushQueue = new LinkedBlockingQueue<>();
         this.objectMapper = new ObjectMapper();
         this.logger = logger;
    }

    public void addRecord(long unixTime, String logLine) {
        this.pushQueue.add(new LokiLogEntry(unixTime, logLine));
    }

    public ArrayList<LokiLogEntry> pollLastEntries() {
        ArrayList<LokiLogEntry> array = new ArrayList<>();
        this.pushQueue.drainTo(array);
        return array;
    }

    public void pushToLoki() throws IOException {
        var lokiStreamData = LokiPushRequest.LokiStreamData.builder()
                .stream(lokifierConfig.configData().labels());

        List<String[]> values = new ArrayList<>();
        pollLastEntries().stream().map(LokiLogEntry::asArray).forEachOrdered(values::add);
        lokiStreamData.values(values);

        final var lokiPushRequest = new LokiPushRequest(List.of(lokiStreamData.build()));

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
                logger.info(String.format("Sent %d logs entries to Loki", values.size()));
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
