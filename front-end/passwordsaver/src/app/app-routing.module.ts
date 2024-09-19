import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuardService } from './shared/services/auth-guard.service';
import { AdminGuardService } from './shared/services/admin-guard.service';

const routes: Routes = [
  {
    path: 'login',
    loadChildren: () => import('./pages/login/login.module').then(m => m.LoginModule)
  },
  {
    path: 'signup',
    loadChildren: () => import('./pages/signup/signup.module').then(m => m.SignupModule)
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
  {
    path: 'servicetypes',
    loadChildren: () => import('./pages/servicetypes/servicetypes.module').then(m => m.ServicetypesModule),
    canActivate: [AuthGuardService, AdminGuardService]
  },
  {
    path: 'servicetypes/:specificView/:value',
    loadChildren: () => import('./pages/servicetypes/servicetypes.module').then(m => m.ServicetypesModule),
    canActivate: [AuthGuardService, AdminGuardService]
  },
  {
    path: 'users',
    loadChildren: () => import('./pages/users/users.module').then(m => m.UsersModule),
    canActivate: [AuthGuardService]
  },
  {
    path: 'users/:specificView/:value',
    loadChildren: () => import('./pages/users/users.module').then(m => m.UsersModule),
    canActivate: [AuthGuardService, AdminGuardService]
  },
  {
    path: 'usertypes',
    loadChildren: () => import('./pages/usertypes/usertypes.module').then(m => m.UsertypesModule),
    canActivate: [AuthGuardService, AdminGuardService]
  },
  {
    path: 'usertypes/:specificView/:value',
    loadChildren: () => import('./pages/usertypes/usertypes.module').then(m => m.UsertypesModule),
    canActivate: [AuthGuardService, AdminGuardService]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
