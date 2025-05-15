import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, MatIconModule, MatButtonModule, RouterModule],
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss']
})
export class SidebarComponent {
  constructor(private router: Router) { }

  navigateToAll(): void {
    this.router.navigate(['/home']);
  }

  navigateToMine(): void {
    this.router.navigate(['/my-auctions']);
  }

  navigateToCreate(): void {
    this.router.navigate(['/create-auction']);
  }

  navigateToEnded(): void {
    this.router.navigate(['/ended-auctions']);
  }

  navigateToWon(): void {
    this.router.navigate(['/won-auctions']);
  }
}
