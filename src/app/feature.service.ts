import { Injectable } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';

import { Observable } from 'rxjs';

import { Feature } from './models/feature';
import { FEATURE_DATA } from './data/feature-data';

@Injectable()
export class FeatureService {

  private limit: number = 2;
  private count: number = 0;
  private pages: number = 0;

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

    // set offset
    let offset = (page - 1) * this.limit;

    return this.http.get(`/api/features?limit=${this.limit}&offset=${offset}`)
      .map(res => {

        // update count and pages
        this.count = +res.headers.get('x-total-count') || 0;
        this.pages = Math.max(Math.ceil(this.count / this.limit), 1);

        // return features as json
        return res.json();

      });
  }

  /**
   * get features count
   * @returns {number}
   */
  getFeatureCount() {
    return this.count;
  }

  /**
   * get page count
   * @returns {number}
   */
  getPageCount() {
    return this.pages;
  }

  /**
   * get feature by name
   * @param {string} name
   * @returns {Feature}
   */
  get(name: string) {
    // return this.features.find(feature => feature.meta.name.toString() === name);
    return this.http.get(`/api/features/${name}`)
      .map(res => res.json());
  }

  /**
   * search for feature by term
   * @param {string} term
   * @returns {Observable<Array<Feature>>}
   */
  searchRaw(term: string) {
    let params: URLSearchParams = new URLSearchParams();
    params.set('q', term);

    /*
    return Observable.of(
      this.features.filter(feature => feature.meta.name.indexOf(params.get('q')) !== -1)
    );
    */
    return this.http.get(`http://localhost:8080/api/features`, {search: params})
      .map(res => res.json());
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
