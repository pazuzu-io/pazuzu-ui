/* tslint:disable:no-unused-variable */

import { TestBed, async } from '@angular/core/testing';
import { RouterModule } from '@angular/router';
import { MaterialModule } from '@angular/material';

import { AppComponent } from './app.component';
import { FeatureListComponent } from './feature-list/feature-list.component';
import { FeatureDetailComponent } from './feature-detail/feature-detail.component';

import { APP_BASE_HREF } from '@angular/common';
import { APP_PROVIDERS } from './app.providers';
import { APP_ROUTES } from './app.routes';

describe('AppComponent', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        FeatureListComponent,
        FeatureDetailComponent
      ],
      imports: [
        RouterModule.forRoot(APP_ROUTES),
        MaterialModule.forRoot()
      ],
      providers: [
        APP_PROVIDERS,
        {provide: APP_BASE_HREF, useValue : '/'}
      ]
    });
    TestBed.compileComponents();
  });

  it('should create the app', async(() => {
    let fixture = TestBed.createComponent(AppComponent);
    let app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it(`should have as app title 'Pazuzu UI'`, async(() => {
    let fixture = TestBed.createComponent(AppComponent);
    let app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('Pazuzu UI');
  }));

  it('should render a toolbar containing the app title', async(() => {
    let fixture = TestBed.createComponent(AppComponent);
    let app = fixture.debugElement.componentInstance;
    fixture.detectChanges();
    let compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('md-toolbar').textContent).toContain(app.title);
  }));
});
