import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyUsertypeComponent } from './modify-usertype.component';

describe('ModifyUsertypeComponent', () => {
  let component: ModifyUsertypeComponent;
  let fixture: ComponentFixture<ModifyUsertypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModifyUsertypeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModifyUsertypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
