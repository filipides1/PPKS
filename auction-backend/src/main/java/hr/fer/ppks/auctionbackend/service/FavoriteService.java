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

    /**
     * Gets the current user ID or returns null if not authenticated
     */
    private Optional<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if there's an authenticated user
        if (authentication == null || !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getName())) {
            return Optional.empty();
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .map(User::getId);
    }

    public List<AuctionItem> getFavoriteAuctionItems() {
        // Get the current user ID if available
        Optional<Long> userIdOpt = getCurrentUserId();

        // If no user is authenticated, return empty list
        if (userIdOpt.isEmpty()) {
            return new ArrayList<>();
        }

        Long userId = userIdOpt.get();
        List<Long> favoriteAuctionIds = favoriteRepository.findByUserId(userId)
                .stream()
                .map(Favorite::getAuctionItemId)
                .collect(Collectors.toList());

        // Fetch the actual auction items
        List<AuctionItem> favoriteItems = new ArrayList<>();
        if (!favoriteAuctionIds.isEmpty()) {
            favoriteItems = auctionItemRepository.findAllById(favoriteAuctionIds);

            // Mark these items as favorites in the response
            favoriteItems.forEach(item -> item.setIsFavorite(true));
        }

        return favoriteItems;
    }

    public void addToFavorites(Long auctionItemId) {
        // Get the current user ID if available
        Optional<Long> userIdOpt = getCurrentUserId();

        // If no user is authenticated, do nothing
        if (userIdOpt.isEmpty()) {
            return;
        }

        Long userId = userIdOpt.get();
        // Check if it's already a favorite
        if (favoriteRepository.findByUserIdAndAuctionItemId(userId, auctionItemId).isEmpty()) {
            Favorite favorite = new Favorite(userId, auctionItemId);
            favoriteRepository.save(favorite);
        }
    }

    @Transactional
    public void removeFromFavorites(Long auctionItemId) {
        // Get the current user ID if available
        Optional<Long> userIdOpt = getCurrentUserId();

        // If no user is authenticated, do nothing
        if (userIdOpt.isEmpty()) {
            return;
        }

        Long userId = userIdOpt.get();
        favoriteRepository.deleteByUserIdAndAuctionItemId(userId, auctionItemId);
    }
}