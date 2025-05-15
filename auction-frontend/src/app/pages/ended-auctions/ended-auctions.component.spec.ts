import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EndedAuctionsComponent } from './ended-auctions.component';

describe('EndedAuctionsComponent', () => {
  let component: EndedAuctionsComponent;
  let fixture: ComponentFixture<EndedAuctionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EndedAuctionsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EndedAuctionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
