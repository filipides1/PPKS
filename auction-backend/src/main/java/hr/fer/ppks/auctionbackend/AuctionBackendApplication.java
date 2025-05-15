package hr.fer.ppks.auctionbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Uncomment this line if you want to enable scheduling
public class AuctionBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuctionBackendApplication.class, args);
    }

}
