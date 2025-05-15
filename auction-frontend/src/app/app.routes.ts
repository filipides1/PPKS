import { Routes } from '@angular/router';

import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/auth/login/login.component';
import { RegisterComponent } from './pages/auth/register/register.component';
import { MyAuctionsComponent } from './pages/my-auctions/my-auctions.component';
import { AuctionDetailsComponent } from './pages/auction-details/auction-details.component';
import { LandingComponent } from './pages/landing/landing.component';
import { CreateAuctionComponent } from './pages/create-auction/create-auction.component';
import { EndedAuctionsComponent } from './pages/ended-auctions/ended-auctions.component';
import { WonAuctionsComponent } from './pages/won-auctions/won-auctions.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
    { path: '', component: LandingComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },

    { path: 'home', component: HomeComponent, canActivate: [authGuard] },
    { path: 'my-auctions', component: MyAuctionsComponent, canActivate: [authGuard] },
    { path: 'auction/:id', component: AuctionDetailsComponent, canActivate: [authGuard] },
    { path: 'ended-auctions', component: EndedAuctionsComponent, canActivate: [authGuard] },
    { path: 'won-auctions', component: WonAuctionsComponent, canActivate: [authGuard] },
    { path: 'create-auction', component: CreateAuctionComponent, canActivate: [authGuard] },

    { path: '**', redirectTo: '' }
];
