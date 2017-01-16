/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { MaterialModule } from '@angular/material';

import { FeatureDetailComponent } from './feature-detail.component';
import { EventBusService } from '../event-bus.service';
import { FeatureService } from '../feature.service';

describe('FeatureDetailComponent', () => {
  let component: FeatureDetailComponent;
  let fixture: ComponentFixture<FeatureDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FeatureDetailComponent
      ],
      imports: [
        RouterTestingModule.withRoutes([
          {path: 'features/detail/test', component: FeatureDetailComponent}
        ]),
        MaterialModule.forRoot()
      ],
      providers: [
        EventBusService,
        FeatureService
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FeatureDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
