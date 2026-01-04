import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../../auth/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.getToken();
  const authReq = token
    ? req.clone({
        setHeaders: { Authorization: `Bearer ${token}` }
      })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      if (error.status === 401) {
        authService.logout();
        router.navigateByUrl('/login');
        return throwError(() => new Error('Votre session a expiré. Merci de vous reconnecter.'));
      }

      if (error.status === 403) {
        return throwError(() => new Error("Vous n'êtes pas autorisé à effectuer cette action."));
      }

      const message =
        error.error?.message ||
        error.statusText ||
        'Une erreur est survenue. Merci de réessayer.';
      return throwError(() => new Error(message));
    })
  );
};
