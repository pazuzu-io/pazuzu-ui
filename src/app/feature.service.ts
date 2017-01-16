import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Feature } from './models/feature';
import { FEATURE_DATA } from './data/feature-data';

@Injectable()
export class FeatureService {

  private features: Array<Feature> = FEATURE_DATA;

  constructor(private http: Http) { }

  getFeatures() {
    return this.features;
    /*
    return this.http.get('http://localhost:8080/api/features')
      .map(res => res.json())
      .map(data => data.items);
    */
  }

  getFeature(name: string) {
    return this.features.find(feature => feature.meta.name.toString() === name);
    /*
    return this.http.get(`http://localhost:8080/api/features/${id}`)
      .map(res => res.json())
      .map(data => data.item);
    */
  }

}
