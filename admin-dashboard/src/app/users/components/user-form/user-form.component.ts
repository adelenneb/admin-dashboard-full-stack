import { Component, EventEmitter, Input, OnChanges, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { User } from '../../models/user.model';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnChanges {
  @Input() user: User | null = null;
  @Output() save = new EventEmitter<User>();
  @Output() cancel = new EventEmitter<void>();

  form: FormGroup;
  submitting = false;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      role: ['USER', Validators.required],
      password: ['']
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['user']) {
      this.form.reset({
        username: this.user?.username ?? '',
        email: this.user?.email ?? '',
        role: this.user?.role ?? 'USER',
        password: ''
      });
    }
  }

  get username() {
    return this.form.get('username');
  }

  get email() {
    return this.form.get('email');
  }

  get role() {
    return this.form.get('role');
  }

  get password() {
    return this.form.get('password');
  }

  submit(): void {
    if (this.form.invalid || (!this.user && !this.password?.value)) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: User = {
      ...this.user,
      ...this.form.value
    };

    if (this.user && !payload.password) {
      delete (payload as any).password;
    }

    this.save.emit(payload);
  }

  onCancel(): void {
    this.cancel.emit();
  }
}
