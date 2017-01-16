/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { HttpModule } from '@angular/http';
import { FeatureService } from './feature.service';

describe('FeatureService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpModule
      ],
      providers: [
        FeatureService
      ]
    });
  });

  it('should ...', inject([FeatureService], (service: FeatureService) => {
    expect(service).toBeTruthy();
  }));
});
