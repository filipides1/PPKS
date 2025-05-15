package hr.fer.ppks.auctionbackend.repository;

import hr.fer.ppks.auctionbackend.model.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuctionItemIdOrderByTimestampDesc(Long auctionItemId);
    List<Bid> findByAuctionItemIdOrderByAmountDesc(Long auctionItemId);
}