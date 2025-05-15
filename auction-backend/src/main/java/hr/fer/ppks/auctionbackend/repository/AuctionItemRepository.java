package hr.fer.ppks.auctionbackend.repository;

import hr.fer.ppks.auctionbackend.model.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, Long> {
    List<AuctionItem> findByEndTimeAfter(LocalDateTime dateTime);

    List<AuctionItem> findByEndTimeBefore(LocalDateTime dateTime);

    List<AuctionItem> findByEndTimeBeforeAndIsCompletedFalse(LocalDateTime dateTime);

    List<AuctionItem> findByWinnerUsernameAndIsCompletedTrue(String username);
}
