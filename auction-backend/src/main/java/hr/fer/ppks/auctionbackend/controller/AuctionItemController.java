package hr.fer.ppks.auctionbackend.controller;

import hr.fer.ppks.auctionbackend.model.AuctionItem;
import hr.fer.ppks.auctionbackend.service.AuctionItemService;
import hr.fer.ppks.auctionbackend.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200") // da Angular može pristupiti
public class AuctionItemController {

    private final AuctionItemService auctionItemService;
    private final FavoriteService favoriteService;

    public AuctionItemController(AuctionItemService auctionItemService, FavoriteService favoriteService) {
        this.auctionItemService = auctionItemService;
        this.favoriteService = favoriteService;
    }

    // Then update your getAllAuctionItems method
    // Update the getAllAuctionItems method to mark favorites based on authenticated user
    @GetMapping("/auction/{id}")
    public AuctionItem getAuctionItemById(@PathVariable Long id) {
        AuctionItem item = auctionItemService.getAuctionItemById(id)
                .orElseThrow(() -> new RuntimeException("Auction item not found"));

        try {
            // Check if this item is in favorites - safely handle missing authentication
            List<AuctionItem> favoriteItems = favoriteService.getFavoriteAuctionItems();
            boolean isFavorite = favoriteItems.stream()
                    .anyMatch(favItem -> favItem.getId().equals(id));

            item.setIsFavorite(isFavorite);
        } catch (Exception e) {
            // If there's an error checking favorites, just continue
            // without marking as favorite
            item.setIsFavorite(false);
        }

        return item;
    }

    @GetMapping("/auction-items/current")
    public List<AuctionItem> getAllCurrentAuctionItems() {
        List<AuctionItem> allItems = auctionItemService.getAllCurrentAuctionItems();

        try {
            // Get the user's favorites to mark items - safely handle missing authentication
            List<AuctionItem> favoriteItems = favoriteService.getFavoriteAuctionItems();
            List<Long> favoriteIds = favoriteItems.stream()
                    .map(AuctionItem::getId)
                    .collect(Collectors.toList());

            // Mark items as favorites
            allItems.forEach(item -> {
                if (favoriteIds.contains(item.getId())) {
                    item.setIsFavorite(true);
                } else {
                    item.setIsFavorite(false);
                }
            });
        } catch (Exception e) {
            // If there's an error checking favorites, just continue
            // without marking any as favorites
            allItems.forEach(item -> item.setIsFavorite(false));
        }

        return allItems;
    }

    @GetMapping("/auction-items/ended")
    public List<AuctionItem> getAllEndedAuctionItems() {
        List<AuctionItem> items = auctionItemService.getAllEndedAuctionItems();
        return items;
    }

    @GetMapping("/auction-items/won")
    public List<AuctionItem> getAuctionsWonByUser(Principal principal) {
        if (principal == null) {
            return new ArrayList<>();
        }
        return auctionItemService.findAuctionsWonByUser(principal.getName());
    }

    // Make sure this endpoint is properly implemented in your controller
    @PostMapping("/auction-items")
    public AuctionItem createAuctionItem(@RequestBody AuctionItem item) {
        // Set current price to starting price if not set
        if (item.getCurrentPrice() == null) {
            item.setCurrentPrice(item.getStartingPrice());
        }
        return auctionItemService.saveAuctionItem(item);
    }
}

