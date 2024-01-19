import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { CreateUserComponent } from './create-user/create-user.component';
import { LandingComponent } from './public/landing/landing.component';

const routes: Routes = [
  {path: '',
   loadChildren: ()=>
   import('./public/public.module').then((m)=>m.PublicModule),
  },
  {path: 'admin',
   loadChildren: ()=>
   import('./admin/admin.module').then((m)=>m.AdminModule),
  },
  {path: 'helper',
   loadChildren: ()=>
   import('./helper/helper.module').then((m)=>m.HelperModule),
  },
  {
    path: 'loginPage',
    component: LoginComponent
  },
  {path: 'CreateUser',
    component: CreateUserComponent
  },
  {
    path: 'landing',
    component: LandingComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
