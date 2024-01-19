import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { HelperRoutingModule } from './helper-routing.module';
import { HelperComponent } from './helper.component';
import { HelperPageComponent } from './helper-page/helper-page.component';

@NgModule({
  declarations: [
    HelperComponent,
    HelperPageComponent
  ],
  imports: [
    CommonModule,
    HelperRoutingModule,
    FormsModule
  ]
})
export class HelperModule { }
