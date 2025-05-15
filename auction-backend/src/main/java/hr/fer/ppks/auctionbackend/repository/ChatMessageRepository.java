package hr.fer.ppks.auctionbackend.repository;

import hr.fer.ppks.auctionbackend.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByAuctionItemIdOrderByTimestampDesc(Long auctionItemId);
}