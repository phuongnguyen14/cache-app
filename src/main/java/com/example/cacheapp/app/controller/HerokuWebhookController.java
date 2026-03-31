package com.example.cacheapp.app.controller;

import com.example.cacheapp.app.dtos.heroku.HerokuWebhookPayload;
import com.example.cacheapp.domain.service.TelegramService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/webhook/heroku")
@RequiredArgsConstructor
public class HerokuWebhookController {

    private final TelegramService telegramService;

    // Optional: Secret to verify the HMAC signature from Heroku. For simplicity, we can do a simple header check or skip validation if empty
    @Value("${heroku.webhook-secret:}")
    private String webhookSecret;

    @PostMapping
    public ResponseEntity<String> receiveWebhook(
            @RequestHeader(value = "Heroku-Webhook-Hmac-SHA256", required = false) String hmacSignature,
            @RequestBody HerokuWebhookPayload payload) {

        log.info("Received Heroku webhook: Resource={}, Action={}", payload.getResource(), payload.getAction());

        // We can skip HMAC validation for now and just forward the notification
        // Note: For production use, you should validate the HMAC signature
        telegramService.notifyHerokuEvent(payload);

        return ResponseEntity.ok("OK");
    }
}
