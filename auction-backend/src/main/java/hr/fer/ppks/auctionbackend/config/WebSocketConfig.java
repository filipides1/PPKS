package hr.fer.ppks.auctionbackend.config;

import hr.fer.ppks.auctionbackend.model.User;
import hr.fer.ppks.auctionbackend.repository.UserRepository;
import hr.fer.ppks.auctionbackend.security.JwtTokenUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:4200")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Bean
    public ChannelInterceptor channelInterceptor(JwtTokenUtil jwtTokenUtil, UserRepository userRepository) {
        return new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    List<String> authHeaders = accessor.getNativeHeader("Authorization");

                    if (authHeaders != null && !authHeaders.isEmpty()) {
                        String authHeader = authHeaders.get(0);
                        if (authHeader.startsWith("Bearer ")) {
                            String token = authHeader.substring(7);
                            String username = jwtTokenUtil.extractUsername(token);

                            if (username != null) {
                                User user = userRepository.findByUsername(username).orElse(null);
                                if (user != null) {
                                    // Create the principal with the username
                                    accessor.setUser(() -> username);
                                }
                            }
                        }
                    }
                }
                return message;
            }
        };
    }
}