import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';

import { Subscription, Observable } from 'rxjs';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-create',
  templateUrl: './feature-create.component.html',
  styleUrls: ['./feature-create.component.css']
})
export class FeatureCreateComponent implements OnInit, OnDestroy {

  private _routerEventsSub: Subscription;

  heading: string;
  feature: Feature;

  options = {
    printMargin: false
  };

  error: string;

  /**
   * go back using Location API
   * @returns {void} nothing
   */
  goBack() {

    // go back
    this.location.back();

  }

  /**
   * on text change handler for ACE editors
   * @param {string} text
   * @param {string} ref
   * @returns {void} nothing
   */
  onTextChanged(text, ref) {
    this.feature[ref] = text !== '' ? text : null;
  }

  /**
   * add dependency if not already defined
   * @param {*} event
   * @returns {void} nothing
   */
  addDependency(event: any) {

    let dependency = event.target.value;

    if (this.feature.meta.dependencies.indexOf(dependency) === -1) {
      this.feature.meta.dependencies.push(dependency);
      event.target.value = '';
    }

  }

  /**
   * remove dependency if found
   * @param {string} dependency
   * @returns {void} nothing
   */
  removeDependency(dependency: string) {

    if (this.feature.meta.dependencies.indexOf(dependency) !== -1) {
      this.feature.meta.dependencies.splice(
        this.feature.meta.dependencies.indexOf(dependency),
        1
      );
    }

  }

  /**
   * go to list
   * @returns {void} nothing
   */
  goToList() {
    this.router.navigate(['/features/list']);
  }

  /**
   * save feature and go to detail view for success otherwise go back to list
   * @param {Feature} feature
   * @returns {void} nothing
   */
  save(feature: Feature) {

    // create feature
    this.featureService.create(feature)
      .subscribe(
        res => {

          // redirect to list
          this.router.navigate(['/features/detail', res.meta.name]);

        },
        err => {

          // set error message
          let body = JSON.parse(err._body);
          this.error = typeof body.title !== 'undefined' ? body.title : null;

          // dismiss error message after timeout
          if (this.error !== null) {
            setTimeout(() => {
              this.error = null;
            }, 5000);
          }

        }
      );

  }

  /**
   * cancel creation and go back to list
   * @returns {void} nothing
   */
  cancel() {
    this.goToList();
  }

  /**
   * constructor
   * @param {Router} router
   * @param {ActivatedRoute} route
   * @param {Location} location
   * @param {EventBusService} eventBusService
   * @param {FeatureService} featureService
   * @returns {void} nothing
   */
  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private location: Location,
    private eventBusService: EventBusService,
    private featureService: FeatureService
  ) {

    this.feature = {
      meta: {
        name: null,
        author: null,
        description: null,
        dependencies: [],
        updated_at: null
      },
      snippet: null,
      test_snippet: null
    };

  }

  /**
   * on init handler
   * load feature
   * @returns {void} nothing
   */
  ngOnInit() {

    // subscribe to param changes if somebody manually changes them
    this._routerEventsSub = this.router.events
      .subscribe(event => {

        // if event is of type NavigationEnd update internal state
        if (event instanceof NavigationEnd) {

          // update heading and title
          this.heading = `Create feature`;
          this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);

        }

      });

  }

  /**
   * on destroy handler
   * @returns {void} nothing
   */
  ngOnDestroy() {

    // unsubscribe
    this._routerEventsSub.unsubscribe();

  }

}
