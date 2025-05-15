import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuctionItem } from '../models/auction-item';
import { catchError, throwError } from 'rxjs';
import { AuthService } from './auth.service';

interface ChatMessage {
    id?: number;
    content: string;
    username: string;
    timestamp: Date;
}

interface Bid {
    id?: number;
    amount: number;
    username: string;
    timestamp: Date;
}

@Injectable({
    providedIn: 'root'
})
export class AuctionService {
    private apiUrl = 'http://localhost:8080/api';

    constructor(private http: HttpClient, private authService: AuthService) { }

    getAllCurrentAuctionItems(): Observable<AuctionItem[]> {
        return this.http.get<AuctionItem[]>(`${this.apiUrl}/auction-items/current`);
    }

    getAllEndedAuctionItems(): Observable<AuctionItem[]> {
        return this.http.get<AuctionItem[]>(`${this.apiUrl}/auction-items/ended`);
    }

    getWonAuctionItems(): Observable<AuctionItem[]> {
        return this.http.get<AuctionItem[]>(`${this.apiUrl}/auction-items/won`);
    }

    createAuction(auctionData: any): Observable<AuctionItem> {
        return this.http.post<AuctionItem>(`${this.apiUrl}/auction-items`, auctionData);
    }

    getAuctionItem(id: number): Observable<AuctionItem> {
        console.log(`Fetching auction item with ID: ${id}`);
        return this.http.get<AuctionItem>(`${this.apiUrl}/auction/${id}`);
    }

    getFavoriteAuctionItems(): Observable<AuctionItem[]> {
        return this.http.get<AuctionItem[]>(`${this.apiUrl}/auctions/favorites`);
    }

    addToFavorites(auctionId: number): Observable<any> {
        if (!this.authService.isLoggedIn()) {
            return throwError(() => new Error('You must be logged in to add favorites'));
        }

        return this.http.post<void>(`${this.apiUrl}/auctions/${auctionId}/favorites`, {})
            .pipe(
                catchError(error => {
                    console.error('Error adding to favorites:', error);
                    return throwError(() => new Error('Failed to add to favorites'));
                })
            );
    }

    removeFromFavorites(auctionId: number): Observable<any> {
        if (!this.authService.isLoggedIn()) {
            return throwError(() => new Error('You must be logged in to remove favorites'));
        }

        return this.http.delete<void>(`${this.apiUrl}/auctions/${auctionId}/favorites`)
            .pipe(
                catchError(error => {
                    console.error('Error removing from favorites:', error);
                    return throwError(() => new Error('Failed to remove from favorites'));
                })
            );
    }

    getBids(auctionId: number): Observable<Bid[]> {
        return this.http.get<Bid[]>(`${this.apiUrl}/auctions/${auctionId}/bids`);
    }

    placeBid(auctionId: number, amount: number): Observable<void> {
        return this.http.post<void>(`${this.apiUrl}/auctions/${auctionId}/bids`, { amount });
    }

    getChatMessages(auctionId: number): Observable<ChatMessage[]> {
        return this.http.get<ChatMessage[]>(`${this.apiUrl}/auctions/${auctionId}/chat`);
    }

    sendChatMessage(auctionId: number, content: string): Observable<any> {
        if (!this.authService.isLoggedIn()) {
            return throwError(() => new Error('You must be logged in to send messages'));
        }

        return this.http.post<any>(`${this.apiUrl}/auctions/${auctionId}/chat`, { content })
            .pipe(
                catchError(error => {
                    console.error('Error sending chat message:', error);
                    return throwError(() => new Error(error.error?.message || 'Failed to send message'));
                })
            );
    }
}