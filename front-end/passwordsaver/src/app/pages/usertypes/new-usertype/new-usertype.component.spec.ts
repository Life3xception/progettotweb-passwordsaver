import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewUsertypeComponent } from './new-usertype.component';

describe('NewUsertypeComponent', () => {
  let component: NewUsertypeComponent;
  let fixture: ComponentFixture<NewUsertypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NewUsertypeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewUsertypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
