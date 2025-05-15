package hr.fer.ppks.auctionbackend.dto;

import java.time.LocalDateTime;

public class ChatMessageResponse {
    private Long id;
    private String content;
    private String username;
    private LocalDateTime timestamp;

    // Constructors
    public ChatMessageResponse() {}

    public ChatMessageResponse(Long id, String content, String username, LocalDateTime timestamp) {
        this.id = id;
        this.content = content;
        this.username = username;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}