package hr.fer.ppks.auctionbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "auction_item_id")
    private Long auctionItemId;

    // Constructors
    public Favorite() {}

    public Favorite(Long userId, Long auctionItemId) {
        this.userId = userId;
        this.auctionItemId = auctionItemId;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public Long getAuctionItemId() { return auctionItemId; }

    public void setId(Long id) { this.id = id; }
    public void setUserId(Long userId) { this.userId = userId; }
    public void setAuctionItemId(Long auctionItemId) { this.auctionItemId = auctionItemId; }
}