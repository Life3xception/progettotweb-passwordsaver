import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PasswordsComponent } from './passwords.component';

const routes: Routes = [
  {
    path: '',
    component: PasswordsComponent
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class PasswordsRoutingModule { }
