package hr.fer.ppks.auctionbackend.service;

import hr.fer.ppks.auctionbackend.dto.ChatMessageResponse;
import hr.fer.ppks.auctionbackend.model.AuctionItem;
import hr.fer.ppks.auctionbackend.model.ChatMessage;
import hr.fer.ppks.auctionbackend.repository.AuctionItemRepository;
import hr.fer.ppks.auctionbackend.repository.ChatMessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final AuctionItemRepository auctionItemRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChatMessageRepository chatMessageRepository,
                       AuctionItemRepository auctionItemRepository,
                       SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.auctionItemRepository = auctionItemRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public List<ChatMessageResponse> getChatMessagesByAuctionId(Long auctionId) {
        return chatMessageRepository.findByAuctionItemIdOrderByTimestampDesc(auctionId)
                .stream()
                .map(message -> new ChatMessageResponse(
                        message.getId(),
                        message.getContent(),
                        message.getUsername(),
                        message.getTimestamp()))
                .collect(Collectors.toList());
    }

    public ChatMessageResponse sendChatMessage(Long auctionId, String content, String username) {
        if (username == null || username.equals("anonymousUser")) {
            throw new RuntimeException("User not authenticated");
        }

        AuctionItem auctionItem = auctionItemRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction item not found"));

        ChatMessage chatMessage = new ChatMessage(content, username, auctionItem);
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        ChatMessageResponse chatMessageResponse = new ChatMessageResponse(
                savedMessage.getId(),
                savedMessage.getContent(),
                savedMessage.getUsername(),
                savedMessage.getTimestamp()
        );

        messagingTemplate.convertAndSend(
                "/topic/auctions/" + auctionId + "/chat",
                chatMessageResponse);

        return chatMessageResponse;
    }
}