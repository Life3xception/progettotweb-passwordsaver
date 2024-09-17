import { NgModule } from '@angular/core';
import { ServicetypesComponent } from './servicetypes.component';
import { RouterModule, Routes } from '@angular/router';
import { NewServicetypeComponent } from './new-servicetype/new-servicetype.component';
import { ModifyServicetypeComponent } from './modify-servicetype/modify-servicetype.component';

const routes: Routes = [
  {
    path: '',
    component: ServicetypesComponent
  },
  {
    path: 'new-servicetype',
    component: NewServicetypeComponent
  },
  {
    path: 'modify-servicetype/:idServiceType',
    component: ModifyServicetypeComponent
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ServicetypesRoutingModule { }
