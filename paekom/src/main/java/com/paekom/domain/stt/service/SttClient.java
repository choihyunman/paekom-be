package com.paekom.domain.stt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SttClient {

    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String aiServerUrl;

    public String requestStt(String s3Key) {
        Map<String, String> body = Map.of("s3_key", s3Key);

        return webClient.post()
                .uri(aiServerUrl + "/api/ai/stt")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(res -> {
                    Map<String, Object> data = (Map<String, Object>) res.get("data");
                    return (String) data.get("transcript");
                })
                .block(); // 동기 방식
    }
}