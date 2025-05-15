// src/app/pages/auction-details/auction-details.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { AuctionService } from '../../services/auction.service';
import { AuctionItem } from '../../models/auction-item';
import { WebSocketService } from '../../services/websocket.service';
import { Subscription } from 'rxjs';
import { ChatMessage } from '../../models/chat-message';
import { Bid } from '../../models/bid';
import { AuthService } from '../../services/auth.service'; // Import AuthService
import { Location } from '@angular/common';

@Component({
  selector: 'app-auction-details',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './auction-details.component.html',
  styleUrls: ['./auction-details.component.scss']
})
export class AuctionDetailsComponent implements OnInit, OnDestroy {
  auctionId!: number;
  auctionItem?: AuctionItem;
  bidForm!: FormGroup;
  chatForm!: FormGroup;
  chatMessages: ChatMessage[] = [];
  bids: Bid[] = [];
  private bidSubscription?: Subscription;
  private chatSubscription?: Subscription;
  private countdownInterval: any;
  countdownDisplay: string = '';
  isCountdownUrgent: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private auctionService: AuctionService,
    private wsService: WebSocketService,
    private fb: FormBuilder,
    private authService: AuthService,
    private location: Location
  ) { }

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.auctionId = +id;
        this.loadAuctionDetails();
        this.connectToWebSockets();
        this.loadChatHistory();
        this.loadBidHistory();
        this.startCountdown();
      }
    });

    this.bidForm = this.fb.group({
      amount: ['', [Validators.required, Validators.min(0.01)]]
    });

    this.chatForm = this.fb.group({
      message: ['', Validators.required]
    });
  }

  ngOnDestroy(): void {
    if (this.bidSubscription) {
      this.bidSubscription.unsubscribe();
    }

    if (this.chatSubscription) {
      this.chatSubscription.unsubscribe();
    }

    if (this.countdownInterval) {
      clearInterval(this.countdownInterval);
    }

    this.wsService.disconnect();
  }

  loadAuctionDetails(): void {
    this.auctionService.getAuctionItem(this.auctionId).subscribe(item => {
      this.auctionItem = item;
      this.loadChatHistory();
      this.loadBidHistory();
    });
  }

  connectToWebSockets(): void {
    this.wsService.disconnect();

    this.wsService.connect();

    setTimeout(() => {
      if (this.bidSubscription) {
        this.bidSubscription.unsubscribe();
      }
      if (this.chatSubscription) {
        this.chatSubscription.unsubscribe();
      }

      this.bidSubscription = this.wsService.subscribeToBids(this.auctionId).subscribe({
        next: (bid) => {
          console.log('Received bid:', bid);
          this.bids.unshift(bid);
          if (this.auctionItem) {
            this.auctionItem.currentPrice = bid.amount;
          }
        },
        error: (err) => console.error('Bid subscription error:', err)
      });

      this.chatSubscription = this.wsService.subscribeToChat(this.auctionId).subscribe({
        next: (message) => {
          console.log('Received chat message:', message);
          this.chatMessages.unshift(message);
        },
        error: (err) => console.error('Chat subscription error:', err)
      });
    }, 1000);
  }

  loadChatHistory(): void {
    this.auctionService.getChatMessages(this.auctionId).subscribe(messages => {
      this.chatMessages = messages;
    });
  }

  loadBidHistory(): void {
    this.auctionService.getBids(this.auctionId).subscribe(bids => {
      this.bids = bids;
    });
  }

  placeBid(): void {
    if (this.bidForm.valid && this.auctionItem) {
      const amount = this.bidForm.value.amount;

      this.auctionService.placeBid(this.auctionId, amount).subscribe({
        next: () => {
          this.bidForm.reset();
          this.bidForm.patchValue({
            amount: this.auctionItem!.currentPrice + 0.5
          });
        },
        error: (error) => {
          const errorMessage = error?.message || 'Error placing bid';
          alert(errorMessage);
        }
      });
    }
  }

  sendMessage(): void {
    if (this.chatForm.valid) {
      const content = this.chatForm.value.message;

      if (!content || content.trim() === '') {
        return;
      }

      this.auctionService.sendChatMessage(this.auctionId, content).subscribe({
        next: () => {
          this.chatForm.reset();
        },
        error: (error) => {
          const errorMessage = error?.message || 'Error sending message';
          alert(errorMessage);
        }
      });
    }
  }

  toggleFavorite(): void {
    if (!this.auctionItem) return;

    if (!this.authService.isLoggedIn()) {
      alert('Please log in to add favorites');
      return;
    }

    const wasAlreadyFavorite = this.auctionItem.isFavorite;
    this.auctionItem.isFavorite = !wasAlreadyFavorite;

    if (this.auctionItem.isFavorite) {
      this.auctionService.addToFavorites(this.auctionId).subscribe({
        error: (error) => {
          this.auctionItem!.isFavorite = wasAlreadyFavorite;
          alert('Error adding to favorites: ' + error.message);
        }
      });
    } else {
      this.auctionService.removeFromFavorites(this.auctionId).subscribe({
        error: (error) => {
          this.auctionItem!.isFavorite = wasAlreadyFavorite;
          alert('Error removing from favorites: ' + error.message);
        }
      });
    }
  }

  goBack(): void {
    this.location.back();
  }

  incrementBid(amount: number): void {
    const currentAmount = this.bidForm.get('amount')?.value || this.auctionItem?.currentPrice;
    const newAmount = Number(currentAmount) + amount;

    this.bidForm.patchValue({
      amount: newAmount
    });
  }

  get isAuctionEnded(): boolean {
    if (!this.auctionItem) return false;
    return new Date(this.auctionItem.endTime) < new Date();
  }

  private startCountdown(): void {
    this.updateCountdown();

    this.countdownInterval = setInterval(() => {
      this.updateCountdown();
    }, 1000);
  }

  private updateCountdown(): void {
    if (!this.auctionItem) return;

    const now = new Date();
    const endTime = new Date(this.auctionItem.endTime);
    const timeRemaining = endTime.getTime() - now.getTime();

    if (timeRemaining <= 0) {
      this.countdownDisplay = '0 sati 0 minuta 0 sekundi';
      this.isCountdownUrgent = false;

      if (this.countdownInterval) {
        clearInterval(this.countdownInterval);
      }

      if (!this.isAuctionEnded) {
        this.loadAuctionDetails();
      }

      return;
    }

    const seconds = Math.floor((timeRemaining / 1000) % 60);
    const minutes = Math.floor((timeRemaining / (1000 * 60)) % 60);
    const hours = Math.floor(timeRemaining / (1000 * 60 * 60));

    this.countdownDisplay = `${hours} sati ${minutes} minuta ${seconds} sekundi`;

    this.isCountdownUrgent = timeRemaining < 2 * 60 * 1000;
  }

}