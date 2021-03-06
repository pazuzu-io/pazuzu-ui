import { Component, OnInit, OnDestroy, EventEmitter } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';

import { Subscription, Observable } from 'rxjs';
import { MaterializeAction } from 'angular2-materialize';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-detail',
  templateUrl: './feature-detail.component.html',
  styleUrls: ['./feature-detail.component.css']
})
export class FeatureDetailComponent implements OnInit, OnDestroy {

  private _routerEventsSub: Subscription;

  name: string;
  heading: string;
  feature: Observable<Feature>;

  options = {
    printMargin: false
  };

  modalDeleteActions = new EventEmitter<string|MaterializeAction>();
  modalDeleteError = null;

  /**
   * go back using Location API
   * @returns {void} nothing
   */
  goBack() {

    // go back
    this.location.back();

  }

  /**
   * open delete modal
   * @returns {void} nothing
   */
  openDeleteModal() {

    // open delete modal to get confirmation first
    this.modalDeleteActions.emit({action: 'modal', params: ['open']});

  }

  /**
   * confirm delete modal
   * @returns {void} nothing
   */
  confirmDeleteModal() {

    // delete feature
    this.featureService.delete(this.name)
      .subscribe(
        res => {

          // close delete modal
          this.modalDeleteActions.emit({action: 'modal', params: ['close']});

          // redirect to list
          this.router.navigate(['/features/list']);

        },
        err => {

          // set error message
          this.modalDeleteError = typeof err.statusText !== 'undefined' ? err.statusText : null;

          // dismiss error message after timeout
          if (this.modalDeleteError !== null) {
            setTimeout(() => {
              this.modalDeleteError = null;
            }, 5000);
          }

        }
      );

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
  ) { }

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

          // get name parameter
          this.name = this.route.snapshot.params['name'];

          // update heading and title
          this.heading = `Feature details for ${this.name}`;
          this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);

          // get feature
          this.featureService.get(this.name)
            .subscribe(
              feature => this.feature = feature,
              () => this.router.navigate(['/features/list'])
            );

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
