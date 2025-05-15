import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import SockJS from 'sockjs-client';
import { Client, IMessage, Stomp } from '@stomp/stompjs';
import { AuthService } from './auth.service';

@Injectable({
    providedIn: 'root'
})
export class WebSocketService {
    private stompClient: any = null;

    constructor(private authService: AuthService) { }

    connect(): void {
        const socket = new SockJS('http://localhost:8080/ws');
        this.stompClient = Stomp.over(socket);

        const headers: any = {};
        const token = this.authService.getToken();
        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }

        this.stompClient.connect(headers, () => {
            console.log('WebSocket Connected');
        }, this.onError);
    }

    disconnect(): void {
        if (this.stompClient && this.stompClient.connected) {
            this.stompClient.disconnect(() => {
                console.log('WebSocket Disconnected');
            });
        }
    }

    onError(error: any): void {
        console.error('WebSocket Error:', error);
    }

    subscribeToBids(auctionId: number): Observable<any> {
        return new Observable(observer => {
            if (!this.stompClient) {
                console.error('STOMP client is null');
                this.connect();
                observer.error('STOMP client not connected - attempting to connect');
                return;
            }

            if (!this.stompClient.connected) {
                console.error('STOMP client not connected');
                observer.error('STOMP client not connected');
                return;
            }

            console.log(`Subscribing to bids for auction ${auctionId}`);

            const subscription = this.stompClient.subscribe(
                `/topic/auctions/${auctionId}/bids`,
                (message: IMessage) => {
                    console.log('Received bid message:', message.body);
                    try {
                        const bid = JSON.parse(message.body);
                        observer.next(bid);
                    } catch (e) {
                        console.error('Error parsing bid message:', e);
                    }
                },
                { id: `bid-sub-${auctionId}` }
            );

            return () => {
                console.log(`Unsubscribing from bids for auction ${auctionId}`);
                if (subscription) {
                    subscription.unsubscribe();
                }
            };
        });
    }

    subscribeToChat(auctionId: number): Observable<any> {
        return new Observable(observer => {
            if (!this.stompClient || !this.stompClient.connected) {
                observer.error('STOMP client not connected');
                return;
            }

            const subscription = this.stompClient.subscribe(
                `/topic/auctions/${auctionId}/chat`,
                (message: IMessage) => {
                    const chatMessage = JSON.parse(message.body);
                    observer.next(chatMessage);
                }
            );

            return () => {
                subscription.unsubscribe();
            };
        });
    }
}