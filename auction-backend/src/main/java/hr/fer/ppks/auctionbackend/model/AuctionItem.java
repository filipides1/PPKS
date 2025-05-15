package hr.fer.ppks.auctionbackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "auction_items") // Explicitly define table name
public class AuctionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "starting_price")
    private Double startingPrice;

    @Column(name = "current_price")
    private Double currentPrice;

    @Column(name = "winner_username")
    private String winnerUsername;

    @Column(name = "is_completed")
    private Boolean isCompleted = false;

    @Transient // This won't be stored in the database
    private Boolean isFavorite = false;

    // Constructors
    public AuctionItem() {}

    public AuctionItem(String name, String description, LocalDateTime startTime, LocalDateTime endTime, Double startingPrice, Double currentPrice, Boolean isCompleted, String winnerUsername) {
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startingPrice = startingPrice;
        this.currentPrice = currentPrice;
        this.isCompleted = isCompleted;
        this.winnerUsername = winnerUsername;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public Double getStartingPrice() { return startingPrice; }
    public Double getCurrentPrice() { return currentPrice; }
    public Boolean getIsFavorite() { return isFavorite; }
    public String getWinnerUsername() { return winnerUsername; }
    public Boolean getIsCompleted() { return isCompleted; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public void setStartingPrice(Double startingPrice) { this.startingPrice = startingPrice; }
    public void setCurrentPrice(Double currentPrice) { this.currentPrice = currentPrice; }
    public void setIsFavorite(Boolean favorite) { this.isFavorite = favorite; }
    public void setWinnerUsername(String winnerUsername) { this.winnerUsername = winnerUsername; }
    public void setIsCompleted(Boolean isCompleted) { this.isCompleted = isCompleted; }
}