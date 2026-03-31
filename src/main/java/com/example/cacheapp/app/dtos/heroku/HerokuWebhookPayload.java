package com.example.cacheapp.app.dtos.heroku;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HerokuWebhookPayload {

    private String id;
    private String action; // e.g., "create", "update", "destroy"
    private String resource; // e.g., "build", "release", "dyno"
    private String actor;
    private OffsetDateTime createdAt;
    private Map<String, Object> data;

}
