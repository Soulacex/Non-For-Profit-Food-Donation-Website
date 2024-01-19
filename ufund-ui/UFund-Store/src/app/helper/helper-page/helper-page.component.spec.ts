import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HelperPageComponent } from './helper-page.component';

describe('HelperPageComponent', () => {
  let component: HelperPageComponent;
  let fixture: ComponentFixture<HelperPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HelperPageComponent]
    });
    fixture = TestBed.createComponent(HelperPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

document.getElementsByClassName('add-btn')[0].addEventListener("click", myPopupFunction);
function myPopupFunction() {
  alert('Hey, you clicked on my button!')
}
});