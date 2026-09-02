package com.agent.email.controller;

import com.agent.email.model.EmailRequest;
import com.agent.email.service.EmailAgentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
public class EmailAgentController {

    private final EmailAgentService emailAgentService;

    public EmailAgentController(EmailAgentService emailAgentService) {

        this.emailAgentService = emailAgentService;
    }

    @PostMapping("/create")
    public String createEmail(@RequestBody EmailRequest request) throws Exception {

        return emailAgentService.createEmailDraft(request);
    }
}