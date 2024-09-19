import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RecoverPasswordsComponent } from './recover-passwords.component';

describe('RecoverPasswordsComponent', () => {
  let component: RecoverPasswordsComponent;
  let fixture: ComponentFixture<RecoverPasswordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RecoverPasswordsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(RecoverPasswordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
