import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean | UrlTree {
    const isAuthed = this.authService.isAuthenticated();
    if (isAuthed) {
      return true;
    }

    this.authService.logout();
    const returnUrl = this.router.routerState.snapshot.url || '/dashboard';
    return this.router.createUrlTree(['/login'], { queryParams: { returnUrl } });
  }
}
