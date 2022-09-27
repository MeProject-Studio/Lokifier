package ru.meproject.lokifier.common.http;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LokiPushRequest {
    private List<LokiStreamData> streams;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    static class LokiStreamData {
        // labels
        private Map<String, String> stream;
        // log entries
        private List<String[]> values;
    }
}
