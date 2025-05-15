import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuctionService } from '../../services/auction.service';
import { SidebarComponent } from '../../components/sidebar/sidebar.component';
import { HeaderComponent } from '../../components/header/header.component';
import { FooterComponent } from '../../components/footer/footer.component';

@Component({
  selector: 'app-create-auction',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    SidebarComponent,
    HeaderComponent,
    FooterComponent
  ],
  templateUrl: './create-auction.component.html',
  styleUrls: ['./create-auction.component.scss']
})
export class CreateAuctionComponent implements OnInit {
  auctionForm!: FormGroup;
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private auctionService: AuctionService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.initForm();
  }

  initForm(): void {

    const now = new Date();

    const endDate = new Date();
    endDate.setDate(endDate.getDate() + 7);

    const formatDate = (date: Date) => {
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0');
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');

      return `${year}-${month}-${day}T${hours}:${minutes}`;
    };

    this.auctionForm = this.fb.group({
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      startTime: [formatDate(now), [Validators.required]],
      endTime: [formatDate(endDate), [Validators.required]],
      startingPrice: ['', [Validators.required, Validators.min(0.01)]]
    });
  }

  get f() {
    return this.auctionForm.controls;
  }

  onSubmit(): void {
    if (this.auctionForm.invalid) {
      return;
    }

    this.isSubmitting = true;

    const auctionData = {
      ...this.auctionForm.value,
      currentPrice: this.auctionForm.value.startingPrice
    };

    this.auctionService.createAuction(auctionData).subscribe({
      next: () => {
        this.router.navigate(['/home']);
      },
      error: (error) => {
        console.error('Error creating auction:', error);
        this.isSubmitting = false;
        alert('Došlo je do greške prilikom kreiranja aukcije. Molimo pokušajte ponovno.');
      }
    });
  }

  cancel(): void {
    this.router.navigate(['/home']);
  }
}