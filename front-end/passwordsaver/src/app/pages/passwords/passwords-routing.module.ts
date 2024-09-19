import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PasswordsComponent } from './passwords.component';
import { NewPasswordComponent } from './new-password/new-password.component';
import { ModifyPasswordComponent } from './modify-password/modify-password.component';
import { RecoverPasswordsComponent } from './recover-passwords/recover-passwords.component';
import { AdminGuardService } from '../../shared/services/admin-guard.service';

const routes: Routes = [
  {
    path: '',
    component: PasswordsComponent
  },
  {
    path: 'new-password',
    component: NewPasswordComponent
  },
  {
    path: 'modify-password/:idPassword',
    component: ModifyPasswordComponent
  },
  {
    path: 'recover-passwords',
    component: RecoverPasswordsComponent,
    canActivate: [AdminGuardService]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PasswordsRoutingModule { }
