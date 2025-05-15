import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

interface AuthResponse {
    token: string;
    username: string;
    userId: number;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth';
    private currentUserSubject = new BehaviorSubject<any>(null);
    public currentUser = this.currentUserSubject.asObservable();

    constructor(
        private http: HttpClient,
        private router: Router
    ) {
        this.loadUserFromStorage();
    }

    private loadUserFromStorage() {
        const userData = localStorage.getItem('currentUser');
        if (userData) {
            const user = JSON.parse(userData);
            if (user && user.token) {
                this.currentUserSubject.next(user);
            }
        }
    }

    register(username: string, password: string): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/register`, { username, password })
            .pipe(
                tap(response => this.handleAuthResponse(response))
            );
    }

    login(username: string, password: string): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/login`, { username, password })
            .pipe(
                tap(response => this.handleAuthResponse(response))
            );
    }

    private handleAuthResponse(response: AuthResponse): void {
        if (response && response.token) {
            localStorage.setItem('currentUser', JSON.stringify(response));
            this.currentUserSubject.next(response);
        }
    }

    logout(): void {
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
        this.router.navigate(['/login']);
    }

    isLoggedIn(): boolean {
        return !!this.currentUserSubject.value;
    }

    getToken(): string | null {
        const currentUser = this.currentUserSubject.value;
        return currentUser ? currentUser.token : null;
    }

    getUsername(): string | null {
        const currentUser = this.currentUserSubject.value;
        return currentUser ? currentUser.username : null;
    }

    getUserId(): number | null {
        const currentUser = this.currentUserSubject.value;
        return currentUser ? currentUser.userId : null;
    }

    refreshToken(): Observable<AuthResponse> {
        const currentUser = this.currentUserSubject.value;

        if (!currentUser || !currentUser.token) {
            return throwError(() => new Error('No token to refresh'));
        }

        return this.http.post<AuthResponse>(`${this.apiUrl}/refresh-token`, { token: currentUser.token })
            .pipe(
                tap(response => this.handleAuthResponse(response)),
                catchError(error => {
                    this.logout();
                    return throwError(() => new Error('Session expired. Please log in again.'));
                })
            );
    }
}