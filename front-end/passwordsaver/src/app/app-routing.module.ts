import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './shared/services/auth-guard.service';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule)
  },
  {
    path: '',
    loadChildren: () => import('./pages/home/home.module').then(m => m.HomeModule),
    canActivate: [AuthGuardService]
  },
  {
    path: 'home',
    redirectTo: '',
    pathMatch: 'full', // fa il redirect a '' se si richiede home
    canActivate: [AuthGuardService]
  },
  {
    path: 'passwords',
    loadChildren: () => import('./pages/passwords/passwords.module').then(m => m.PasswordsModule),
    canActivate: [AuthGuardService]
  },
  {
    path: 'passwords/:specificView/:value',
    loadChildren: () => import('./pages/passwords/passwords.module').then(m => m.PasswordsModule),
    canActivate: [AuthGuardService]
  },
  {
    path: 'services',
    loadChildren: () => import('./pages/services/services.module').then(m => m.ServicesModule),
    canActivate: [AuthGuardService]
  },
  {
    path: 'services/:specificView/:value',
    loadChildren: () => import('./pages/services/services.module').then(m => m.ServicesModule),
    canActivate: [AuthGuardService]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
