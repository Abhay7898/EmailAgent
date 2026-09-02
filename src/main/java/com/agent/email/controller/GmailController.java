package com.agent.email.controller;

import com.agent.email.model.DraftRequest;
import com.agent.email.service.GmailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gmail")
public class GmailController {

    private final GmailService gmailService;

    public GmailController(GmailService gmailService) {
        this.gmailService = gmailService;
    }

    @GetMapping("/test")
    public String testGmail() throws Exception {

        return gmailService.testConnection();
    }

    @PostMapping("/draft")
    public String createDraft(@RequestBody DraftRequest request) throws Exception {

        return gmailService.createDraft(request.getTo(), request.getSubject(), request.getBody());
    }
}