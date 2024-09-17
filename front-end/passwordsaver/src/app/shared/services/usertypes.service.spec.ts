import { TestBed } from '@angular/core/testing';

import { UsertypesService } from './usertypes.service';

describe('UsertypesService', () => {
  let service: UsertypesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UsertypesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
