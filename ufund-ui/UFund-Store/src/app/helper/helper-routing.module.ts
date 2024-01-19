import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HelperComponent } from './helper.component';
import { HelperPageComponent } from './helper-page/helper-page.component';

const routes: Routes = [
  {
    path: '',
    component: HelperComponent,
    children: [
      { path: 'helper', component: HelperPageComponent },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HelperRoutingModule { }
