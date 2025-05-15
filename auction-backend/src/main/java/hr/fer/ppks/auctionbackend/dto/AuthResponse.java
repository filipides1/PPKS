package hr.fer.ppks.auctionbackend.dto;

public class AuthResponse {
    private String token;
    private String username;
    private Long userId;

    public AuthResponse(String token, String username, Long userId) {
        this.token = token;
        this.username = username;
        this.userId = userId;
    }

    // Getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}