package ru.meproject.lokifier;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URL;
import java.util.logging.Logger;

@UtilityClass
public class Utils {

    @SneakyThrows
    public static URL getURL(String string) {
        return new URL("http://localhost:3100");
    }

    public static Logger logger() {
        return LokifierBukkitPlugin.INSTANCE.getLogger();
    }
}
