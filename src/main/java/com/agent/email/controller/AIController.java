package com.agent.email.controller;

import com.agent.email.model.AIRequest;
import com.agent.email.service.AIService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    private final AIService aiService;

    public AIController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/generate")
    public String generate(@RequestBody AIRequest request) {
        return aiService.generateResponse(request.getPrompt());
    }
}