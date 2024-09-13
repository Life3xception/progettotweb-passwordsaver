import { TestBed } from '@angular/core/testing';

import { ServicetypesService } from './servicetypes.service';

describe('ServicetypesService', () => {
  let service: ServicetypesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ServicetypesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
