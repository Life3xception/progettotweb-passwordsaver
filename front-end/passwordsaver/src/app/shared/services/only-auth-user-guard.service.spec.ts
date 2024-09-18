import { TestBed } from '@angular/core/testing';

import { OnlyAuthUserGuardService } from './only-auth-user-guard.service';

describe('OnlyAuthUserGuardService', () => {
  let service: OnlyAuthUserGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OnlyAuthUserGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
