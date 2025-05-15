package hr.fer.ppks.auctionbackend.service;

import hr.fer.ppks.auctionbackend.model.AuctionItem;
import hr.fer.ppks.auctionbackend.model.Bid;
import hr.fer.ppks.auctionbackend.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuctionSchedulerService {

    private final AuctionItemService auctionItemService;
    private final BidRepository bidRepository;

    @Autowired
    public AuctionSchedulerService(AuctionItemService auctionItemService, BidRepository bidRepository) {
        this.auctionItemService = auctionItemService;
        this.bidRepository = bidRepository;
    }

    @Scheduled(fixedDelay = 60000) // Run every minute
    public void processEndedAuctions() {
        List<AuctionItem> endedAuctions = auctionItemService.findEndedButNotCompletedAuctions();

        for (AuctionItem auction : endedAuctions) {
            // Find the highest bid for this auction
            List<Bid> bids = bidRepository.findByAuctionItemIdOrderByAmountDesc(auction.getId());

            if (!bids.isEmpty()) {
                // Set the winner to the highest bidder
                Bid winningBid = bids.get(0);
                auction.setWinnerUsername(winningBid.getUsername());
            }

            // Mark as completed
            auction.setIsCompleted(true);
            auctionItemService.saveAuctionItem(auction);
        }
    }
}
