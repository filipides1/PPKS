import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuctionListComponent } from '../../components/auction-list/auction-list.component';
import { RouterModule } from '@angular/router';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-my-auctions',
  standalone: true,
  imports: [CommonModule, AuctionListComponent, RouterModule, SidebarComponent,
    HeaderComponent, FooterComponent
  ],
  templateUrl: './my-auctions.component.html',
  styleUrl: './my-auctions.component.scss'
})
export class MyAuctionsComponent {

}
