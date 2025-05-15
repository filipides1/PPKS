package hr.fer.ppks.auctionbackend.repository;

import hr.fer.ppks.auctionbackend.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndAuctionItemId(Long userId, Long auctionItemId);
    void deleteByUserIdAndAuctionItemId(Long userId, Long auctionItemId);
}