import { TestBed } from '@angular/core/testing';

import { HelperMessageService } from './helper-message.service';

describe('HelperMessageService', () => {
  let service: HelperMessageService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HelperMessageService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
