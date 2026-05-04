package com.payflow.paymentservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "webhook_registrations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WebHookRegistration {

    @Id
    private String id;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String events;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static WebHookRegistration create(
            String clientId,String url,String events
    ){
        if (url==null || url.isBlank())
            throw new IllegalArgumentException("Webhook URL boş olamaz");

        if (!url.startsWith("http"))
            throw new IllegalArgumentException("Geçersiz URL: " + url);

        WebHookRegistration webhook =  new WebHookRegistration();

        webhook.id = UUID.randomUUID().toString();
        webhook.clientId = clientId;
        webhook.url = url;
        webhook.events = events;
        webhook.active = true;
        webhook.createdAt = LocalDateTime.now();
        return webhook;
    }
    public void deactivate() {
        this.active = false;
    }

    public boolean listensTo(String event){
        return this.events.contains(event);
    }
}
