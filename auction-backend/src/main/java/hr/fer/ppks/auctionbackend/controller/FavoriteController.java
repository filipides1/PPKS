package hr.fer.ppks.auctionbackend.controller;

import hr.fer.ppks.auctionbackend.model.AuctionItem;
import hr.fer.ppks.auctionbackend.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/auctions/favorites")
    public List<AuctionItem> getFavoriteAuctionItems() {
        return favoriteService.getFavoriteAuctionItems();
    }

    @PostMapping("/auctions/{auctionId}/favorites")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long auctionId) {
        // Check authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        favoriteService.addToFavorites(auctionId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/auctions/{auctionId}/favorites")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long auctionId) {
        // Check authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        favoriteService.removeFromFavorites(auctionId);
        return ResponseEntity.ok().build();
    }
}