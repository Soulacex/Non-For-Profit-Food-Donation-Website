import { Component, OnInit } from '@angular/core';
import { waitForAsync } from '@angular/core/testing';
import { delay } from 'rxjs';
import { HelperMessageService } from 'src/app/helper-message.service';
import { HelperMessage } from 'src/app/HelperMessage';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})

export class LandingComponent implements OnInit {
  helperMessages: HelperMessage[] = []

  randomMessage: HelperMessage = {"id":2000000,"username":"FundsInHighPlaces Team-7G","message":"Hello Everyone!"};

  constructor(private messageService: HelperMessageService) { }

  ngOnInit() {
    this.getAllMessages().then(() => {
      this.getRandomMessage();
    });
  }
  
  getAllMessages(): Promise<void> {
    return new Promise((resolve) => {
      this.messageService.getAllMessages().subscribe(messages => {
        this.helperMessages = messages;
        resolve();
      });
    });
  }
  
  getRandomMessage(): void {
    let randomIndex = this.helperMessages.length;
    setInterval(() => {
      randomIndex--;
      if (randomIndex < 0) {
        randomIndex = this.helperMessages.length; 
      }
      this.messageService.searchMessageId(randomIndex).subscribe(message => this.randomMessage = message);
    }, 3000)
  }  
  
}