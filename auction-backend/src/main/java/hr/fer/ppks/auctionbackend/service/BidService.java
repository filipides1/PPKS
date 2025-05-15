package hr.fer.ppks.auctionbackend.service;

import hr.fer.ppks.auctionbackend.dto.BidResponse;
import hr.fer.ppks.auctionbackend.model.AuctionItem;
import hr.fer.ppks.auctionbackend.model.Bid;
import hr.fer.ppks.auctionbackend.model.User;
import hr.fer.ppks.auctionbackend.repository.AuctionItemRepository;
import hr.fer.ppks.auctionbackend.repository.BidRepository;
import hr.fer.ppks.auctionbackend.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BidService {

    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Temporary username for development
    private static final String TEMP_USERNAME = "user1";

    public BidService(BidRepository bidRepository,
                      AuctionItemRepository auctionItemRepository,
                      SimpMessagingTemplate messagingTemplate,
                      UserRepository userRepository) {
        this.bidRepository = bidRepository;
        this.auctionItemRepository = auctionItemRepository;
        this.messagingTemplate = messagingTemplate;
        this.userRepository = userRepository;
    }

    public List<BidResponse> getBidsByAuctionId(Long auctionId) {
        return bidRepository.findByAuctionItemIdOrderByTimestampDesc(auctionId)
                .stream()
                .map(bid -> new BidResponse(
                        bid.getId(),
                        bid.getAmount(),
                        bid.getUsername(),
                        bid.getTimestamp()))
                .collect(Collectors.toList());
    }

    // Update placeBid method
// Update the placeBid method to use authentication
    @Transactional
    public BidResponse placeBid(Long auctionId, Double amount, String username) {
        // Fetch the auction item
        AuctionItem auctionItem = auctionItemRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction item not found"));

        // Validate the bid amount
        if (amount <= auctionItem.getCurrentPrice()) {
            throw new IllegalArgumentException("Bid amount must be greater than current price");
        }

        // Create and save the bid
        Bid bid = new Bid(amount, username, auctionItem);
        Bid savedBid = bidRepository.save(bid);

        // Update the auction item's current price
        auctionItem.setCurrentPrice(amount);
        auctionItemRepository.save(auctionItem);

        // Create response DTO
        BidResponse bidResponse = new BidResponse(
                savedBid.getId(),
                savedBid.getAmount(),
                savedBid.getUsername(),
                savedBid.getTimestamp()
        );

        // Broadcast the new bid to all subscribers
        messagingTemplate.convertAndSend(
                "/topic/auctions/" + auctionId + "/bids",
                bidResponse);

        return bidResponse;
    }
}