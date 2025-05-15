import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';

export const AuthInterceptor: HttpInterceptorFn = (req, next) => {
    const authService = inject(AuthService);
    const token = authService.getToken();

    console.log(`Request to ${req.url}`);

    if (token) {
        console.log(`Adding auth token to request: ${req.url}`);
        console.log(`Token: ${token}`);
        const authReq = req.clone({
            headers: req.headers.set('Authorization', `Bearer ${token}`)
        });
        return next(authReq);
    }

    return next(req);
};