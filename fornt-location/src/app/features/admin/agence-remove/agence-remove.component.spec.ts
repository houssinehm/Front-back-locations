import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AgenceRemoveComponent } from './agence-remove.component';

describe('AgenceRemoveComponent', () => {
  let component: AgenceRemoveComponent;
  let fixture: ComponentFixture<AgenceRemoveComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AgenceRemoveComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AgenceRemoveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
