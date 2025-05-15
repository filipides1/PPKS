package hr.fer.ppks.auctionbackend.dto;

import java.time.LocalDateTime;

public class BidResponse {
    private Long id;
    private Double amount;
    private String username;
    private LocalDateTime timestamp;

    // Constructors
    public BidResponse() {}

    public BidResponse(Long id, Double amount, String username, LocalDateTime timestamp) {
        this.id = id;
        this.amount = amount;
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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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