import { TestBed } from '@angular/core/testing';

import { ModifyUserGuardService } from './modify-user-guard.service';

describe('ModifyUserGuardService', () => {
  let service: ModifyUserGuardService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ModifyUserGuardService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
