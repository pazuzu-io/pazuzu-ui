import { Injectable } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';

import { Observable } from 'rxjs';

import { Feature } from './models/feature';
import { FEATURE_DATA } from './data/feature-data';

@Injectable()
export class FeatureService {

  private size: number = 10;
  private features: Array<Feature> = FEATURE_DATA;

  /**
   * constructor
   * @param {Http} http
   * @returns {void} nothing
   */
  constructor(
    private http: Http
  ) { }

  /**
   * get all features for given page
   * @param {number} page
   * @returns {Observable<Array<Feature>>}
   */
  getAll(page = 1) {
    let offset = (page - 1) * this.size;
    return Observable.of(this.features.slice(offset * this.size, offset * this.size + this.size));
    /*
    return this.http.get('http://localhost:8080/api/features')
      .map(res => res.json());
    */
  }

  /**
   * get number of features
   * @returns {number}
   */
  getFeatureCount() {
    return this.features.length;
  }

  /**
   * get number of pages
   * @returns {number}
   */
  getPageCount() {
    return Math.ceil(this.features.length / this.size);
  }

  /**
   * get feature by name
   * @param {string} name
   * @returns {Feature}
   */
  get(name: string) {
    return this.features.find(feature => feature.meta.name.toString() === name);
    /*
    return this.http.get(`http://localhost:8080/api/features/${name}`)
      .map(res => res.json());
    */
  }

  /**
   * search for feature by term
   * @param {string} term
   * @returns {Observable<Array<Feature>>}
   */
  searchRaw(term: string) {
    let params: URLSearchParams = new URLSearchParams();
    params.set('q', term);

    return Observable.of(
      this.features.filter(feature => feature.meta.name.indexOf(params.get('q')) !== -1)
    );
    /*
    return this.http.get(`http://localhost:8080/api/features`, {search: params})
      .map(res => res.json());
    */
  }

  /**
   * search for feature with debounce and distinct detection
   * @param {string} term
   * @param {number} debounceMs
   * @returns {Observable<Array<Feature>>}
   */
  search(term: Observable<string>, debounceMs = 400) {
    return term
      .debounceTime(debounceMs)
      .distinctUntilChanged()
      .switchMap(t => this.searchRaw(t));
  }

}
