/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { MaterialModule } from '@angular/material';

import { FeatureListComponent } from './feature-list.component';
import { EventBusService } from '../event-bus.service';
import { FeatureService } from '../feature.service';

describe('FeatureListComponent', () => {
  let component: FeatureListComponent;
  let fixture: ComponentFixture<FeatureListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FeatureListComponent
      ],
      imports: [
        RouterTestingModule.withRoutes([
          {path: 'features/list', component: FeatureListComponent}
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
    fixture = TestBed.createComponent(FeatureListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
