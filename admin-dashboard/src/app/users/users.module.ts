import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { UserListComponent } from './components/user-list/user-list.component';
import { UserFormComponent } from './components/user-form/user-form.component';

const routes: Routes = [{ path: '', component: UserListComponent }];

@NgModule({
  declarations: [UserListComponent, UserFormComponent],
  imports: [CommonModule, ReactiveFormsModule, RouterModule.forChild(routes), MatButtonModule, MatIconModule],
  exports: [UserListComponent, UserFormComponent]
})
export class UsersModule {}
