import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UsersService } from '../../services/users.service';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit, OnDestroy {
  users: User[] = [];
  loading = false;
  error = '';
  selectedUser: User | null = null;
  showForm = false;
  private subscription = new Subscription();

  constructor(private usersService: UsersService) {}

  ngOnInit(): void {
    this.fetchUsers();
  }

  trackById(_index: number, user: User): string | number {
    return user.id ?? _index;
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  fetchUsers(): void {
    this.loading = true;
    this.error = '';
    const sub = this.usersService.getUsers().subscribe({
      next: (users) => {
        this.users = users;
        this.loading = false;
      },
      error: () => {
        this.error = 'Unable to load users.';
        this.loading = false;
      }
    });
    this.subscription.add(sub);
  }

  onEdit(user: User): void {
    this.selectedUser = { ...user };
    this.showForm = true;
  }

  onDelete(user: User): void {
    if (!user.id) {
      return;
    }
    this.loading = true;
    const sub = this.usersService.deleteUser(user.id).subscribe({
      next: () => this.fetchUsers(),
      error: () => {
        this.error = 'Unable to delete user.';
        this.loading = false;
      }
    });
    this.subscription.add(sub);
  }

  onCreate(): void {
    this.selectedUser = null;
    this.showForm = true;
  }

  onSave(user: User): void {
    this.loading = true;
    this.error = '';
    const request$ = user.id
      ? this.usersService.updateUser(user.id, user)
      : this.usersService.createUser(user);

    const sub = request$.subscribe({
      next: () => {
        this.showForm = false;
        this.selectedUser = null;
        this.fetchUsers();
      },
      error: () => {
        this.error = 'Unable to save user.';
        this.loading = false;
      }
    });

    this.subscription.add(sub);
  }

  onCancel(): void {
    this.showForm = false;
    this.selectedUser = null;
  }
}
