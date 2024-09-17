import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsertypesComponent } from './usertypes.component';

describe('UsertypesComponent', () => {
  let component: UsertypesComponent;
  let fixture: ComponentFixture<UsertypesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsertypesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UsertypesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
