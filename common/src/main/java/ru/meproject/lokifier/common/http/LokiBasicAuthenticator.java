package ru.meproject.lokifier.common.http;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.meproject.lokifier.common.LokifierConfig;

import java.io.IOException;

public class LokiBasicAuthenticator implements Authenticator {
    private final LokifierConfig lokifierConfig;

    public LokiBasicAuthenticator(LokifierConfig config) {
        this.lokifierConfig = config;
    }

    @Nullable
    @Override
    public Request authenticate(@Nullable Route route, @NotNull Response response) throws IOException {
        if (response.request().header("Authorization") != null) {
            // Give up, we've already attempted to authenticate.
            return null;
        }

        if (lokifierConfig.configData().basicAuth().enable()) {
            // Basic Auth is set up
            final var credential = Credentials.basic(
                    lokifierConfig.configData().basicAuth().username(),
                    lokifierConfig.configData().basicAuth().password());
            return response.request().newBuilder()
                    .header("Authorization", credential)
                    .build();
        }
        return null;
    }
}
