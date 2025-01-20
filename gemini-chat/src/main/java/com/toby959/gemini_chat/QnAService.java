package com.toby959.gemini_chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Map;
import java.util.Objects;

@Service
public class QnAService {
    // Access to APIkey and URL [Gemini]

    @Value("${gemini.api.url}")
    private String geminiApiUrl;
    @Value("${gemini.api.key}")
    private String geminiApiKey;

    private final WebClient webClient;

    public QnAService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }


   // @PostMapping("/ask")
    public String getAnswer(String question) {
        // Construct the request payload
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[] {
                                Map.of("text", question)
                        })
                }
        );

// {
//  "contents": [{
//    "parts":[{"text": "Explain how AI works"}]
//    }]
//   }
        // Make API Call
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .retryWhen(Retry.backoff(3, Duration.ofSeconds(5))) // Reintenta 3 veces con un retraso exponencial inicial de 5 segundos
                .block();

        // Return response
        return response;
    }

}
