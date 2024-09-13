import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ServicesComponent } from './services.component';
import { NewServiceComponent } from './new-service/new-service.component';
import { ModifyServiceComponent } from './modify-service/modify-service.component';

const routes: Routes = [
  {
    path: '',
    component: ServicesComponent
  },
  {
    path: 'new-service',
    component: NewServiceComponent
  },
  {
    path: 'modify-service/:idService',
    component: ModifyServiceComponent
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ServicesRoutingModule { }
