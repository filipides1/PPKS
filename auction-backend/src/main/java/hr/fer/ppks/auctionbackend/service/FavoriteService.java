package hr.fer.ppks.auctionbackend.service;

import hr.fer.ppks.auctionbackend.model.AuctionItem;
import hr.fer.ppks.auctionbackend.model.Favorite;
import hr.fer.ppks.auctionbackend.model.User;
import hr.fer.ppks.auctionbackend.repository.AuctionItemRepository;
import hr.fer.ppks.auctionbackend.repository.FavoriteRepository;
import hr.fer.ppks.auctionbackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final UserRepository userRepository;

    public FavoriteService(FavoriteRepository favoriteRepository,
                           AuctionItemRepository auctionItemRepository,
                           UserRepository userRepository) {
        this.favoriteRepository = favoriteRepository;
        this.auctionItemRepository = auctionItemRepository;
        this.userRepository = userRepository;
    }

    private Optional<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            return Optional.empty();
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .map(User::getId);
    }

    public List<AuctionItem> getFavoriteAuctionItems() {
        Optional<Long> userIdOpt = getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Long userId = userIdOpt.get();
        List<Long> favoriteAuctionIds = favoriteRepository.findByUserId(userId)
                .stream()
                .map(Favorite::getAuctionItemId)
                .collect(Collectors.toList());

        List<AuctionItem> favoriteItems = new ArrayList<>();
        if (!favoriteAuctionIds.isEmpty()) {
            favoriteItems = auctionItemRepository.findAllById(favoriteAuctionIds);

            favoriteItems.forEach(item -> item.setIsFavorite(true));
        }

        return favoriteItems;
    }

    public void addToFavorites(Long auctionItemId) {
        Optional<Long> userIdOpt = getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            return;
        }

        Long userId = userIdOpt.get();
        if (favoriteRepository.findByUserIdAndAuctionItemId(userId, auctionItemId).isEmpty()) {
            Favorite favorite = new Favorite(userId, auctionItemId);
            favoriteRepository.save(favorite);
        }
    }

    @Transactional
    public void removeFromFavorites(Long auctionItemId) {
        Optional<Long> userIdOpt = getCurrentUserId();

        if (userIdOpt.isEmpty()) {
            return;
        }

        Long userId = userIdOpt.get();
        favoriteRepository.deleteByUserIdAndAuctionItemId(userId, auctionItemId);
    }
}