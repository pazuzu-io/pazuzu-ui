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

import { FeatureCreateComponent } from './feature-create.component';
import { AceEditorDirective } from '../ace-editor.directive';

import { EventBusService } from '../event-bus.service';
import { FeatureService } from '../feature.service';

describe('FeatureCreateComponent', () => {
  let component: FeatureCreateComponent;
  let fixture: ComponentFixture<FeatureCreateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FeatureCreateComponent,
        AceEditorDirective
      ],
      imports: [
        RouterTestingModule.withRoutes([
          {path: 'features/create', component: FeatureCreateComponent}
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
            snapshot: { }
          }
        }
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FeatureCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
