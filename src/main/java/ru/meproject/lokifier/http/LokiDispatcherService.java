package ru.meproject.lokifier.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import okhttp3.*;
import ru.meproject.lokifier.console.LokiLogEntry;
import ru.meproject.lokifier.LokifierBukkitPlugin;
import ru.meproject.lokifier.LokifierConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class LokiDispatcherService {
    public static final MediaType MEDIA_TYPE_JSON = MediaType.get("application/json; charset=utf-8");

    private final LinkedBlockingQueue<LokiLogEntry> pushQueue;
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;
    private final LokifierConfig lokifierConfig;

    public LokiDispatcherService(LokifierConfig config) {
         this.client =  new OkHttpClient();
         this.pushQueue = new LinkedBlockingQueue<>();
         this.objectMapper = new ObjectMapper();
         this.lokifierConfig = config;
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

        final var lokiPushRequest = new LokiPushRequest(ImmutableList.of(lokiStreamData.build()));

        final var lokiPushEndpoint = new HttpUrl.Builder()
                .query(lokifierConfig.configData().lokiUrl())
                .addPathSegments("loki/api/v1/push")
                .build();

        final var request = new Request.Builder()
                .url(lokiPushEndpoint)
                .post(RequestBody.create(objectMapper.writeValueAsString(lokiPushRequest), MEDIA_TYPE_JSON))
                .build();

        try (var response = client.newCall(request).execute()) {
            LokifierBukkitPlugin.INSTANCE.getLogger().info("Sent data to Loki. Response: " + response.body().string());
        }
    }
}
