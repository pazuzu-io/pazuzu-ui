/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '@angular/material';

import { FeatureDetailComponent } from './feature-detail.component';
import { FeatureListComponent } from '../feature-list/feature-list.component';

import { APP_BASE_HREF } from '@angular/common';
import { APP_PROVIDERS } from '../app.providers';
import { APP_ROUTES } from '../app.routes';

describe('FeatureDetailComponent', () => {
  let component: FeatureDetailComponent;
  let fixture: ComponentFixture<FeatureDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FeatureDetailComponent,
        FeatureListComponent
      ],
      imports: [
        RouterModule.forRoot(APP_ROUTES),
        MaterialModule.forRoot()
      ],
      providers: [
        APP_PROVIDERS,
        {provide: APP_BASE_HREF, useValue : '/'}
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
