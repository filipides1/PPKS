import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuctionService } from '../../services/auction.service';
import { AuctionItem } from '../../models/auction-item';
import { AuctionListComponent } from '../../components/auction-list/auction-list.component';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-won-auctions',
  standalone: true,
  imports: [CommonModule, AuctionListComponent, SidebarComponent, HeaderComponent, FooterComponent],
  templateUrl: './won-auctions.component.html',
  styleUrls: ['./won-auctions.component.scss']
})
export class WonAuctionsComponent implements OnInit {
  wonAuctions: AuctionItem[] = [];

  constructor(private auctionService: AuctionService) { }

  ngOnInit(): void {
    this.loadWonAuctions();
  }

  loadWonAuctions(): void {
    this.auctionService.getWonAuctionItems().subscribe(items => {
      this.wonAuctions = items;
    });
  }
}