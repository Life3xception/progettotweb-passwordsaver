import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UsertypesComponent } from './usertypes.component';
import { NewUsertypeComponent } from './new-usertype/new-usertype.component';
import { ModifyUsertypeComponent } from './modify-usertype/modify-usertype.component';

const routes: Routes = [
  {
    path: '',
    component: UsertypesComponent
  },
  {
    path: 'new-usertype',
    component: NewUsertypeComponent
  },
  {
    path: 'modify-usertype/:idUserType',
    component: ModifyUsertypeComponent
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsertypesRoutingModule { }
