package hr.fer.ppks.auctionbackend.controller;

import hr.fer.ppks.auctionbackend.dto.BidRequest;
import hr.fer.ppks.auctionbackend.dto.BidResponse;
import hr.fer.ppks.auctionbackend.service.BidService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class BidController {

    private final BidService bidService;

    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    @GetMapping("/auctions/{auctionId}/bids")
    public List<BidResponse> getBidsByAuctionId(@PathVariable Long auctionId) {
        return bidService.getBidsByAuctionId(auctionId);
    }

    @PostMapping("/auctions/{auctionId}/bids")
    public ResponseEntity<Void> placeBid(@PathVariable Long auctionId, @RequestBody BidRequest bidRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        bidService.placeBid(auctionId, bidRequest.getAmount(), username);
        return ResponseEntity.ok().build();
    }
}