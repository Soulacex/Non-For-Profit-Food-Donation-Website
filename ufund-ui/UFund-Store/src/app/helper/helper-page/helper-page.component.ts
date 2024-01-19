import { Component, OnInit } from '@angular/core';
import { Needs } from 'src/app/needs';
import { HelperService } from 'src/app/helper.service';
import { Cupboard } from 'src/app/cupboard.service';
import { LoginComponent } from 'src/app/login/login.component';
import { SupportedNeed } from 'src/app/SupportedNeeds';
import { Notification } from 'src/app/notification';
import { NotificationService } from 'src/app/notification.service';
import { AdminService } from 'src/app/admin.service';
import { HelperMessage } from 'src/app/HelperMessage';
import { HelperMessageService } from 'src/app/helper-message.service';

@Component({
  selector: 'app-helper-page',
  templateUrl: './helper-page.component.html',
  styleUrls: ['./helper-page.component.scss']
})
export class HelperPageComponent implements OnInit {

  cupboardNeeds: Needs[] = [];
  fundingBasket: SupportedNeed[] = [];
  supportedNeeds: SupportedNeed[] = [];
  selectedNeed?: Needs;
  searchTerm: string = '';
  currentUser: string = LoginComponent.currentUser;

  constructor(private helperService: HelperService, private adminService: AdminService, private cupboardService: Cupboard,
    private notificationService: NotificationService, private messageService: HelperMessageService) {}


  ngOnInit(): void {
    this.getCupboardNeeds();
    this.getFundingBasket();
    const storedSupportedNeeds = localStorage.getItem('supportedNeeds');
    if (storedSupportedNeeds) {
      this.supportedNeeds = JSON.parse(storedSupportedNeeds);
    }
  }

  getCupboardNeeds(): void {
    this.cupboardService.getAllNeeds().subscribe(
      (needs: Needs[]) => {
        this.cupboardNeeds = needs;
      }
    );
    this.adminService.getNeeds().subscribe(needs => this.cupboardNeeds = needs);
  }

  getFundingBasket():void{
    this.helperService.getSupportedNeeds(this.currentUser).subscribe(needs => needs.forEach( need => {this.supportedNeeds.push(need);
                                                                                                      this.fundingBasket.push(need);}))
  }

  searchCupboardNeeds(): void {
    if (!this.searchTerm.trim()) {
      this.getCupboardNeeds();
      return;
    }
  
    this.cupboardNeeds = this.cupboardNeeds.filter(need => need.name.toLowerCase().includes(this.searchTerm.toLowerCase()));
  }  
 

  resetCupboardSearch(): void {
    this.searchTerm = '';
    this.getCupboardNeeds();
  }

  addToBasket(cupboardNeed: Needs): void {
    let existingNeed = this.supportedNeeds.find(need => need.need_name === cupboardNeed.name);
    if (existingNeed) {
      if (existingNeed.quantity < cupboardNeed.quantity) {
        existingNeed.quantity++;
      } else {
        alert("Cannot add more of this need. Reached max limit in the cupboard.");
      }
    } else {
      let suppNeed = this.makeSupportedNeed(cupboardNeed.name, 1);
      this.helperService.addSupportedNeed(suppNeed).subscribe(need => {
        if (need.name === null){
          alert("Failed to add need");
        } else {
          this.supportedNeeds.push(suppNeed);
          this.fundingBasket.push(suppNeed);
          localStorage.setItem('supportedNeeds', JSON.stringify(this.supportedNeeds));
        }
      });
    }
  }    

  removeFromBasket(suppNeed: SupportedNeed): void {
  this.helperService.removeSupportedNeed(suppNeed).subscribe();
  const index = this.supportedNeeds.findIndex(need => need.need_name === suppNeed.need_name);
  if (index !== -1) {
    if(suppNeed.quantity>1){
    suppNeed.quantity = suppNeed.quantity-1;
    this.supportedNeeds[index] = suppNeed;
    this.fundingBasket[index] = suppNeed;
    localStorage.setItem('supportedNeeds', JSON.stringify(this.supportedNeeds));
    }else{
      this.supportedNeeds.splice(index,1);
      this.fundingBasket.splice(index,1);
      localStorage.setItem('supportedNeeds', JSON.stringify(this.supportedNeeds));
    }
  }
}
  
  
checkout(): void {
  if(this.supportedNeeds.length===0){
    alert("Failed: Check Funding Basket")
  } else {
    let username = this.currentUser; 
    this.helperService.checkOut(this.currentUser).subscribe(
      _ => {
        for (let suppNeed of this.supportedNeeds) {
          let cupboardNeed = this.cupboardNeeds.find(n => n.name === suppNeed.need_name);
          if (cupboardNeed) {
            cupboardNeed.quantity -= suppNeed.quantity;
          }
        }
        this.supportedNeeds = [];
        localStorage.removeItem('supportedNeeds');
        const message = 'Checkout Successful!';
        alert(message);

        if (this.fundingBasket.length!=0){
          this.fundingBasket.forEach(fundneed => 
          this.notificationService.createNotification
          ({username, "need": fundneed.need_name,"quantity": fundneed.quantity} as Notification).subscribe());
        }
      }
    );
    this.getCupboardNeeds(); 
  }
}
    

  makeSupportedNeed(needName: string, needQuant: number): SupportedNeed{
    needName = needName.trim();
    let SuppNeed = {
      username: this.currentUser,
      need_name: needName,
      quantity: needQuant
    }
    return SuppNeed as SupportedNeed;
    
  }
  
  createNotification(){
    let username = this.currentUser
    if(this.fundingBasket.length!=0){
      this.fundingBasket.forEach(fundneed => 
      this.notificationService.createNotification
      ({username,"need": fundneed.need_name,"quantity": fundneed.quantity} as Notification).subscribe());
    }
  }

  addHelperMessage(message:string): void{
    let username = this.currentUser;
    username = username.trim();
    message = message.trim();
    if (!message) { return; }
    this.messageService.addMessage({username,message} as HelperMessage).subscribe();
  }
}