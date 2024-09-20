import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsertypeFormComponent } from './usertype-form.component';

describe('UsertypeFormComponent', () => {
  let component: UsertypeFormComponent;
  let fixture: ComponentFixture<UsertypeFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UsertypeFormComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UsertypeFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
