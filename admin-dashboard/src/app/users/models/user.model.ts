export interface User {
  id?: string | number;
  username: string;
  email: string;
  role: 'ADMIN' | 'USER' | string;
  password?: string;
  createdAt?: string;
  updatedAt?: string;
}
