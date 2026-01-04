import { Component, OnDestroy, OnInit } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { AuthService } from '../../auth/auth.service';

interface NavItem {
  label: string;
  icon: string;
  route: string;
}

@Component({
  selector: 'app-dashboard-layout',
  templateUrl: './dashboard-layout.component.html',
  styleUrls: ['./dashboard-layout.component.css']
})
export class DashboardLayoutComponent implements OnInit, OnDestroy {
  navItems: NavItem[] = [];

  readonly isHandset$: Observable<boolean>;
  isHandset = false;
  private subscription = new Subscription();
  dashboardTitle = 'Dashboard';

  constructor(
    private breakpointObserver: BreakpointObserver,
    private authService: AuthService,
    private router: Router
  ) {
    this.isHandset$ = this.breakpointObserver.observe(Breakpoints.Handset).pipe(
      map((result) => result.matches),
      shareReplay()
    );

    this.subscription.add(
      this.isHandset$.subscribe((value) => {
        this.isHandset = value;
      })
    );
  }

  ngOnInit(): void {
    const roles = this.authService.getUserRoles();
    const primaryRole = roles[0] ?? 'USER';
    this.dashboardTitle = primaryRole === 'ADMIN' ? 'Admin Dashboard' : 'User Dashboard';

    this.navItems = [{ label: 'Home', icon: 'dashboard', route: '/dashboard' }];
    if (roles.includes('ADMIN')) {
      this.navItems.push({ label: 'Users', icon: 'group', route: '/users' });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigateByUrl('/login');
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }
}
