package ru.meproject.lokifier.common;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.net.URL;

@UtilityClass
public class Utils {

    @SneakyThrows
    public static URL getURL(String string) {
        return new URL("http://localhost:3100");
    }
}
