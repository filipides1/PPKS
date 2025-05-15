// src/app/components/auction-list/auction-list.component.ts
import { Component, OnInit, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuctionService } from '../../services/auction.service';
import { AuctionItem } from '../../models/auction-item';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-auction-list',
  standalone: true,
  imports: [CommonModule, RouterModule, ReactiveFormsModule],
  templateUrl: './auction-list.component.html',
  styleUrls: ['./auction-list.component.scss']
})
export class AuctionListComponent implements OnInit {
  @Input() favoritesOnly = false;
  @Input() isEndedAuctions = false;
  @Input() auctionItems: AuctionItem[] = [];

  constructor(
    private auctionService: AuctionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if (this.auctionItems.length === 0) {
      if (this.favoritesOnly) {
        this.loadFavoriteAuctions();
      } else if (this.isEndedAuctions) {
        this.loadEndedAuctions();
      } else {
        this.loadAllAuctions();
      }
    }
  }

  loadAllAuctions(): void {
    this.auctionService.getAllCurrentAuctionItems().subscribe(items => {
      this.auctionItems = items.sort((a, b) => a.name.localeCompare(b.name));
    });
  }

  loadEndedAuctions(): void {
    this.auctionService.getAllEndedAuctionItems().subscribe(items => {
      this.auctionItems = items.sort((a, b) => a.name.localeCompare(b.name));
    });
  }

  loadFavoriteAuctions(): void {
    this.auctionService.getFavoriteAuctionItems().subscribe(items => {
      this.auctionItems = items.sort((a, b) => a.name.localeCompare(b.name));
    });
  }

  toggleFavorite(item: AuctionItem, event: MouseEvent): void {
    event.stopPropagation();

    item.isFavorite = !item.isFavorite;

    if (item.isFavorite) {
      this.auctionService.addToFavorites(item.id).subscribe(() => {

      });
    } else {
      this.auctionService.removeFromFavorites(item.id).subscribe(() => {

        if (this.favoritesOnly) {
          this.auctionItems = this.auctionItems.filter(i => i.id !== item.id);
        }
      });
    }
  }

  viewDetails(itemId: number, event: MouseEvent): void {
    event.stopPropagation();

    this.router.navigate(['/auction', itemId]);
  }
}