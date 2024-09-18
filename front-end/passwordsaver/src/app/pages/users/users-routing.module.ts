import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsersComponent } from './users.component';
import { NewUserComponent } from './new-user/new-user.component';
import { ModifyUserComponent } from './modify-user/modify-user.component';
import { AdminGuardService } from '../../shared/services/admin-guard.service';
import { ModifyUserGuardService } from '../../shared/services/modify-user-guard.service';

const routes: Routes = [
  {
    path: '',
    component: UsersComponent,
    canActivate: [AdminGuardService]
  },
  {
    path: 'new-user',
    component: NewUserComponent,
    canActivate: [AdminGuardService]
  },
  {
    path: 'modify-user/:idUser',
    component: ModifyUserComponent,
    canActivate: [ModifyUserGuardService]
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }
