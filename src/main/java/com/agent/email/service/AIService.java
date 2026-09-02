package com.agent.email.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AIService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateResponse(String prompt) {

        String ollamaUrl = "http://localhost:11434/api/generate";

        Map<String, Object> request = new HashMap<>();

        request.put("model", "llama3.2");
        request.put("prompt", prompt);

        // Tell Ollama that we want JSON
        request.put("format", "json");

        // Get complete response instead of streaming
        request.put("stream", false);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(ollamaUrl, entity, Map.class);

        if (response.getBody() == null) {
            throw new RuntimeException("No response received from Ollama.");
        }

        Object responseText = response.getBody().get("response");

        if (responseText == null) {
            throw new RuntimeException("Ollama response field is missing.");
        }

        return responseText.toString();
    }
}