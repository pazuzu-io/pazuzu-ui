/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';
import { RouterTestingModule } from '@angular/router/testing';
import { BaseRequestOptions, Http, HttpModule } from '@angular/http';
import { MockBackend } from '@angular/http/testing';
import { FormsModule } from '@angular/forms';

import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { FeatureDetailComponent } from './feature-detail.component';
import { AceEditorDirective } from '../ace-editor.directive';

import { EventBusService } from '../event-bus.service';
import { FeatureService } from '../feature.service';

describe('FeatureDetailComponent', () => {
  let component: FeatureDetailComponent;
  let fixture: ComponentFixture<FeatureDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FeatureDetailComponent,
        AceEditorDirective
      ],
      imports: [
        RouterTestingModule.withRoutes([
          {path: 'features/detail/java', component: FeatureDetailComponent}
        ]),
        HttpModule,
        FormsModule
      ],
      providers: [
        EventBusService,
        FeatureService,
        MockBackend,
        BaseRequestOptions,
        {
          provide: Http,
          useFactory: (backend, options) => new Http(backend, options),
          deps: [MockBackend, BaseRequestOptions]
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              params: Observable.from([{'name': 'java'}])
            }
          }
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FeatureDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', async(() => {
    expect(component).toBeTruthy();
  }));
});
