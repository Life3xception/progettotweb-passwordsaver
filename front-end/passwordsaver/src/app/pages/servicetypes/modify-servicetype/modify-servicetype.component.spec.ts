import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifyServicetypeComponent } from './modify-servicetype.component';

describe('ModifyServicetypeComponent', () => {
  let component: ModifyServicetypeComponent;
  let fixture: ComponentFixture<ModifyServicetypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModifyServicetypeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModifyServicetypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
