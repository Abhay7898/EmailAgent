package com.agent.email.service;

import com.agent.email.model.EmailRequest;
import com.agent.email.model.EmailResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class EmailAgentService {

    private final AIService aiService;
    private final GmailService gmailService;
    private final ObjectMapper objectMapper;

    public EmailAgentService(AIService aiService, GmailService gmailService, ObjectMapper objectMapper) {

        this.aiService = aiService;
        this.gmailService = gmailService;
        this.objectMapper = objectMapper;
    }

    public String createEmailDraft(EmailRequest request) throws Exception {

        String prompt = """
                You are an AI email writing agent.
                
                The user will provide a natural-language instruction.
                
                Your job is to understand the user's instruction and
                create a complete professional email.
                
                USER INSTRUCTION:
                %s
                
                RECIPIENT:
                %s
                
                EMAIL TONE:
                %s
                
                RULES:
                
                1. Understand the user's request carefully.
                2. Write the email based only on information provided
                   by the user.
                3. Do not invent personal information, experience,
                   skills, companies, qualifications, or achievements.
                4. If the user mentions technologies, include the
                   relevant technologies naturally in the email.
                5. Generate a professional subject based on the request.
                6. Write a concise and natural email.
                7. Do not use placeholders such as [Name],
                   [Company Name], [Job Title], etc.
                8. If the user provides a name or signature,
                   use it exactly as requested.
                9. Do not add information that the user did not provide.
                10. Do not include markdown formatting.
                11. Do not include explanations.
                12. Return ONLY valid JSON.
                13. The JSON must contain exactly two fields:
                    "subject"
                    "body"
                
                Example output format:
                
                {
                  "subject": "Application for Java Developer",
                  "body": "Hello ... "
                }
                """.formatted(request.getPrompt(), request.getTo(), request.getTone());

        String aiResponse = aiService.generateResponse(prompt);

        EmailResponse emailResponse = objectMapper.readValue(aiResponse, EmailResponse.class);

        return gmailService.createDraft(request.getTo(), emailResponse.getSubject(), emailResponse.getBody());
    }
}