import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewServicetypeComponent } from './new-servicetype.component';

describe('NewServicetypeComponent', () => {
  let component: NewServicetypeComponent;
  let fixture: ComponentFixture<NewServicetypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NewServicetypeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NewServicetypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
