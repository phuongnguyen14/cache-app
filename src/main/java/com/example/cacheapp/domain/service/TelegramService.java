package com.example.cacheapp.domain.service;

import com.example.cacheapp.app.dtos.heroku.HerokuWebhookPayload;
import com.example.cacheapp.config.TelegramProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TelegramService {

    private final TelegramProperties telegramProperties;
    private final RestTemplate restTemplate;

    public TelegramService(TelegramProperties telegramProperties, RestTemplateBuilder restTemplateBuilder) {
        this.telegramProperties = telegramProperties;
        this.restTemplate = restTemplateBuilder.build();
    }

    public void sendMessage(String text) {
        if (!StringUtils.hasText(telegramProperties.getBotToken()) || !StringUtils.hasText(telegramProperties.getChatId())) {
            log.warn("Telegram bot token or chat ID is not configured. Skipping message sending.");
            return;
        }

        String url = String.format("https://api.telegram.org/bot%s/sendMessage", telegramProperties.getBotToken());

        Map<String, Object> body = new HashMap<>();
        body.put("chat_id", telegramProperties.getChatId());
        body.put("text", text);
        body.put("parse_mode", "Markdown");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            restTemplate.postForObject(url, request, String.class);
            log.info("Message sent to Telegram successfully");
        } catch (Exception e) {
            log.error("Failed to send message to Telegram", e);
        }
    }

    public void notifyHerokuEvent(HerokuWebhookPayload payload) {
        if (payload == null) {
            return;
        }

        try {
            String action = payload.getAction();
            String resource = payload.getResource();
            
            // Format basic message
            StringBuilder sb = new StringBuilder();
            sb.append("🚀 *Heroku Notification*\n");
            sb.append("Resource: `").append(resource).append("`\n");
            sb.append("Action: `").append(action).append("`\n");
            
            if (payload.getData() != null) {
                if ("release".equalsIgnoreCase(resource)) {
                    sb.append("Version: `").append(payload.getData().get("version")).append("`\n");
                    sb.append("Status: `").append(payload.getData().get("status")).append("`\n");
                    sb.append("Description: `").append(payload.getData().get("description")).append("`\n");
                } else if ("dyno".equalsIgnoreCase(resource)) {
                    sb.append("App ID: `").append(payload.getData().get("app")).append("`\n");
                    sb.append("Process: `").append(payload.getData().get("type")).append("`\n");
                } else if ("build".equalsIgnoreCase(resource)) {
                	sb.append("Status: `").append(payload.getData().get("status")).append("`\n");
                    sb.append("User: `").append(payload.getData().get("user")).append("`\n");
                }
            }
            
            sendMessage(sb.toString());
        } catch (Exception e) {
            log.error("Failed to format and send Heroku notification", e);
        }
    }
}
