import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { LayoutModule } from '@angular/cdk/layout';
import { NgChartsModule } from 'ng2-charts';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { MatButtonModule } from '@angular/material/button';
import { MatDividerModule } from '@angular/material/divider';
import { MatCardModule } from '@angular/material/card';
import { DashboardLayoutComponent } from './layout/dashboard-layout.component';
import { HomeComponent } from './home/home.component';

const routes: Routes = [
  {
    path: '',
    component: DashboardLayoutComponent,
    children: [{ path: '', component: HomeComponent }]
  }
];

@NgModule({
  declarations: [DashboardLayoutComponent, HomeComponent],
  imports: [
    CommonModule,
    LayoutModule,
    RouterModule.forChild(routes),
    NgChartsModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
    MatListModule,
    MatButtonModule,
    MatDividerModule,
    MatCardModule
  ],
  exports: [DashboardLayoutComponent, HomeComponent]
})
export class DashboardModule {}
