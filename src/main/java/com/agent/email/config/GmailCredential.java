package com.agent.email.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.HttpTransport;

public class GmailCredential {

    private final HttpTransport httpTransport;
    private final Credential credential;

    public GmailCredential(HttpTransport httpTransport, Credential credential) {

        this.httpTransport = httpTransport;
        this.credential = credential;
    }

    public HttpTransport getHttpTransport() {
        return httpTransport;
    }

    public Credential getCredential() {
        return credential;
    }
}