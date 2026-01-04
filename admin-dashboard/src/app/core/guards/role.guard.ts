import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { AuthService } from '../../auth/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    if (!this.authService.isAuthenticated()) {
      return this.router.parseUrl('/login');
    }

    const allowedRoles = (route.data?.['roles'] as string[]) ?? [];
    if (allowedRoles.length === 0) {
      return true;
    }

    const userRoles = this.authService.getUserRoles();
    const hasRole = allowedRoles.some((role) => userRoles.includes(role));

    return hasRole ? true : this.router.parseUrl('/dashboard');
  }
}
