import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { URLSearchParams } from '@angular/http';

import { Subscription, Observable } from 'rxjs';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-list',
  templateUrl: './feature-list.component.html',
  styleUrls: ['./feature-list.component.css']
})
export class FeatureListComponent implements OnInit, OnDestroy {

  private _queryParamsSub: Subscription;
  private _totalSub: Subscription;

  heading: string;

  limitOptions = [10, 20, 50];

  features: Observable<Array<Feature>>;
  params: URLSearchParams;

  total = 0;
  totalMapping: any = {
    '=0': 'No features',
    '=1': 'One feature',
    'other': '# features'
  };

  page = 1;
  pages = 1;
  pagerItems = [1];

  terms = '';

  /**
   * update pagination
   * @returns {void} nothing
   */
  private _updatePagination() {

    // current page
    this.page = Math.ceil((+this.params.get('offset') + 1) / +this.params.get('limit'));

    // number of pages
    this.pages = Math.ceil(this.total / +this.params.get('limit'));

    // if we got invalid values reset to initial values
    if (this.page > this.pages && this.pages !== 0) {
      this.router.navigate(['features/list']);
    }

    // pager items
    this.pagerItems = Array.apply(null, new Array(this.pages)).map((_, i) => i + 1);

  }

  /**
   * constructor
   * @param {Router} router
   * @param {ActivatedRoute} route
   * @param {EventBusService} eventBusService
   * @param {FeatureService} featureService
   * @returns {void} nothing
   */
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private eventBusService: EventBusService,
    private featureService: FeatureService
  ) {

    // update heading and title
    this.heading = 'Feature list';
    this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);

    // initialize params
    this.params = new URLSearchParams();

  }

  /**
   * update given parameter
   * @param {string} key
   * @param {any} value
   * @returns {void} nothing
   */
  updateParam(key: string, value: any) {

    // save parameter
    this.params.set(key, value.toString());

    // safety check for offset
    if (+this.params.get('limit') >= this.total) {
      this.params.set('offset', '0');
    }

    // update route
    // TODO: maybe there is a more sophisticated way of giving URLSearchParams to router.navigate
    this.router.navigate(
      ['features/list'],
      {
        queryParams: {
          offset: this.params.get('offset'),
          limit: this.params.get('limit'),
          names: this.params.get('names') !== '' ? this.params.get('names') : null,
          author: this.params.get('author') !== '' ? this.params.get('author') : null
        }
      }
    );

  }

  /**
   * search for feature by given target value (target.value)
   * @param {*} event
   * @returns {void} nothing
   */
  search(event: any) {

    // blur input field
    event.target.blur();

    // TODO: change to "q" after API is changed

    // set names parameter accordingly
    this.updateParam('names', event.target.value);

  }

  /**
   * go to given page
   * @param {number} page
   * @returns {void} nothing
   */
  goTo(page: number) {

    // idle if we are already on the same page
    if (page >= 1 && page <= this.pages && page !== this.page) {

      this.updateParam(
        'offset',
        (+this.params.get('limit') * (page - 1)).toString()
      );

    }

  }

  /**
   * on init handler
   * @returns {void} nothing
   */
  ngOnInit() {

    // subscribe to query param changes
    this._queryParamsSub = this.route.queryParams
      .subscribe(queryParams => {

        // set query params
        this.params.set('offset', queryParams['offset'] || '0');
        this.params.set('limit', queryParams['limit'] || '10');
        this.params.set('names', queryParams['names'] || null);
        this.params.set('author', queryParams['author'] || null);

        // update pagination
        this._updatePagination();

        // get features
        this.features = this.featureService.list(this.params);

      });

    // subscribe to total number of features
    this._totalSub = this.featureService.total()
      .subscribe(total => {

        this.total = total;

        // update pagination
        this._updatePagination();

      });

  }

  /**
   * on destroy handler
   * @returns {void} nothing
   */
  ngOnDestroy() {

    // unsubscribe
    this._queryParamsSub.unsubscribe();
    this._totalSub.unsubscribe();

  }

}
