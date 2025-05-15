package hr.fer.ppks.auctionbackend.controller;

import hr.fer.ppks.auctionbackend.dto.ChatMessageRequest;
import hr.fer.ppks.auctionbackend.dto.ChatMessageResponse;
import hr.fer.ppks.auctionbackend.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/auctions/{auctionId}/chat")
    public List<ChatMessageResponse> getChatMessagesByAuctionId(@PathVariable Long auctionId) {
        return chatService.getChatMessagesByAuctionId(auctionId);
    }

    @PostMapping("/auctions/{auctionId}/chat")
    public ResponseEntity<Void> sendChatMessage(
            @PathVariable Long auctionId,
            @RequestBody ChatMessageRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        chatService.sendChatMessage(auctionId, request.getContent(), username);
        return ResponseEntity.ok().build();
    }
}