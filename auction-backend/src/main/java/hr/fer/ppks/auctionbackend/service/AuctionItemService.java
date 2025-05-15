package hr.fer.ppks.auctionbackend.service;

import hr.fer.ppks.auctionbackend.model.AuctionItem;
import hr.fer.ppks.auctionbackend.repository.AuctionItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuctionItemService {

    private final AuctionItemRepository auctionItemRepository;

    public AuctionItemService(AuctionItemRepository auctionItemRepository) {
        this.auctionItemRepository = auctionItemRepository;
    }

    public List<AuctionItem> getAllAuctionItems() {
        return auctionItemRepository.findAll();
    }

    public Optional<AuctionItem> getAuctionItemById(Long id) {
        return auctionItemRepository.findById(id);
    }

    public AuctionItem saveAuctionItem(AuctionItem item) {
        return auctionItemRepository.save(item);
    }

    public List<AuctionItem> getAllCurrentAuctionItems() {
        LocalDateTime now = LocalDateTime.now();
        return auctionItemRepository.findByEndTimeAfter(now);
    }

    public List<AuctionItem> getAllEndedAuctionItems() {
        LocalDateTime now = LocalDateTime.now();
        return auctionItemRepository.findByEndTimeBefore(now);
    }

    public List<AuctionItem> findEndedButNotCompletedAuctions() {
        LocalDateTime now = LocalDateTime.now();
        return auctionItemRepository.findByEndTimeBeforeAndIsCompletedFalse(now);
    }

    public List<AuctionItem> findAuctionsWonByUser(String username) {
        return auctionItemRepository.findByWinnerUsernameAndIsCompletedTrue(username);
    }
}

