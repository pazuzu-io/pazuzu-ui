import { Injectable } from '@angular/core';
import { Http } from '@angular/http';

import { Feature } from './models/feature';
import { FEATURE_DATA } from './data/feature-data';

@Injectable()
export class FeatureService {

  private size: number = 1;
  private features: Array<Feature> = FEATURE_DATA;

  constructor(private http: Http) { }

  getFeatures(page = 1) {
    let offset = (page - 1) * this.size;
    return this.features.slice(offset * this.size, offset * this.size + this.size);
    /*
    return this.http.get('http://localhost:8080/api/features')
      .map(res => res.json())
      .map(data => data.items);
    */
  }

  getFeaturesCount() {
    return this.features.length;
  }

  getFeaturesPageCount() {
    return Math.ceil(this.features.length / this.size);
  }

  getFeature(name: string) {
    return this.features.find(feature => feature.meta.name.toString() === name);
    /*
    return this.http.get(`http://localhost:8080/api/features/${name}`)
      .map(res => res.json())
      .map(data => data.item);
    */
  }

}
