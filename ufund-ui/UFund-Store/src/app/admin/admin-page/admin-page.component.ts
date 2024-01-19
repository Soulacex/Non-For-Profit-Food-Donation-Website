import { Component, OnInit } from '@angular/core';
import { AdminService } from 'src/app/admin.service';
import { Needs } from 'src/app/needs';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { min } from 'rxjs';
import { Cupboard } from 'src/app/cupboard.service';
import { NotificationService } from 'src/app/notification.service';
import { Notification } from 'src/app/notification';

@Component({
  selector: 'app-admin-page',
  templateUrl: './admin-page.component.html',
  styleUrls: ['./admin-page.component.scss']
})
export class AdminPageComponent implements OnInit{
  cupboardNeeds: Needs[] = [];
  notifications: Notification[] = [];
  form: FormGroup = this.fb.group({
    needName: ['',Validators.required],
    needCost: ['',Validators.required],
    needType: ['',Validators.required],
    needQuant: ['',Validators.required]
  });
  updateForm: FormGroup = this.fb.group({
    updateName: ['',Validators.required],
    updateCost: ['',Validators.required],
    updateType: ['',Validators.required],
    updateQuant: ['',Validators.required]
  });

  constructor(private adminService: AdminService, private fb: FormBuilder, private cupboardService: Cupboard, private notificationService: NotificationService){ };

  ngOnInit(): void {
      this.getNeeds();
      this.subscribeToCupboardUpdates();
      this.getAllNotifications();
  }

  getNeeds(): void {
      this.adminService.getNeeds().subscribe((Needs) => 
      { this.cupboardNeeds = Needs;
    });
  }

  subscribeToCupboardUpdates(): void {
    this.cupboardService.getCupboardNeedsObservable().subscribe((cupboardNeeds: Needs[]) => {
      for (const updatedNeed of cupboardNeeds) {
        const existingNeedIndex = this.cupboardNeeds.findIndex(n => n.name === updatedNeed.name);
  
        if (existingNeedIndex !== -1) {
          this.cupboardNeeds[existingNeedIndex] = updatedNeed;
        } else {
          this.cupboardNeeds.push(updatedNeed);
        }
      }
    });
  }

  reset(resetForm: FormGroup): void{
    resetForm.reset();
  }

  add(needName: string, needCost: number, needType: string, needQuant: number): void {
    let needToAdd = this.makeNewNeed(needName, needCost, needType, needQuant);
    if(needCost<=0 || needQuant <= 0){
      alert("Invlaid Need: Check cost or quantitiy")
    }else{
    this.adminService.addNeed(needToAdd as Needs).subscribe((need) => {
      this.cupboardNeeds.push(need);
      this.cupboardService.updateCupboardNeeds(this.cupboardNeeds);
    
    })};
  }

  delete(need: Needs): void {
    this.cupboardNeeds = this.cupboardNeeds.filter((n) => n !== need);
    this.adminService.deleteNeed(need.name).subscribe(() => {
      this.cupboardService.updateCupboardNeeds(this.cupboardNeeds);
    });
  }
  
  update(needName: string, needCost: number, needType: string, needQuant: number): void {
    if(needCost<=0 || needQuant <= 0){
      alert("Invalid inputs detected: valid values applied, invalid values discarded")
    }
    let updatedNeed = this.makeNewNeed(needName, needCost, needType, needQuant);
    this.adminService.updateNeed(updatedNeed as Needs).subscribe(() => {
      this.getNeeds();
    });
  }

  makeNewNeed(needName: string, needCost: number, needType: string, needQuant: number): Needs {
    needName = needName.trim();
    needType = needType.trim();

    let need = {
      name: needName,
      cost: needCost,
      type: needType,
      quantity: needQuant,
    };
    return need as Needs;
  }

  getAllNotifications(): void{
    this.notificationService.getAllNotifications().subscribe(notifs => this.notifications = notifs);
  }
}