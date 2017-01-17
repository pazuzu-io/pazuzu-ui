import { Injectable } from '@angular/core';
import { Http, URLSearchParams } from '@angular/http';

import { Observable } from 'rxjs';

import { Feature } from './models/feature';

@Injectable()
export class FeatureService {

  private limit: number = 10;
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
   * trigger request with given search params
   * @param {URLSearchParams} params
   * @returns {Observable<Array<Feature>>}
   */
  request(params: URLSearchParams) {
    return this.http.get(`/api/features`, {search: params})
      .map(res => {

        // update count and pages
        this.count = +res.headers.get('x-total-count') || 0;
        this.pages = Math.max(Math.ceil(this.count / this.limit), 1);

        // return features as json
        return res.json();

      });
  }

  /**
   * get all features for given page and limit
   * @param {number} page
   * @param {number} limit
   * @returns {Observable<Array<Feature>>}
   */
  getAll(page = 1, limit = 10) {

    // set limit
    this.limit = limit;

    // set offset
    let offset = (page - 1) * limit;

    // set search params
    let params: URLSearchParams = new URLSearchParams();
    params.set('limit', limit.toString());
    params.set('offset', offset.toString());

    // trigger request
    return this.request(params);

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

}
