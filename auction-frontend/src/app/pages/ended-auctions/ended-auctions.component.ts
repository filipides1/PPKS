import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuctionService } from '../../services/auction.service';
import { AuctionItem } from '../../models/auction-item';
import { AuctionListComponent } from '../../components/auction-list/auction-list.component';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-ended-auctions',
  standalone: true,
  imports: [CommonModule, AuctionListComponent, SidebarComponent, HeaderComponent, FooterComponent],
  templateUrl: './ended-auctions.component.html',
  styleUrls: ['./ended-auctions.component.scss']
})
export class EndedAuctionsComponent implements OnInit {
  endedAuctions: AuctionItem[] = [];

  constructor(private auctionService: AuctionService) { }

  ngOnInit(): void {
    this.loadEndedAuctions();
  }

  loadEndedAuctions(): void {
    this.auctionService.getAllEndedAuctionItems().subscribe(items => {
      this.endedAuctions = items;
    });
  }
}