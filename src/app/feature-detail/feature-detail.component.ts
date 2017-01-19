import { Component, OnInit, OnDestroy, AfterViewInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute, NavigationEnd } from '@angular/router';
import { Location } from '@angular/common';

import { Subscription, Observable } from 'rxjs';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-detail',
  templateUrl: './feature-detail.component.html',
  styleUrls: ['./feature-detail.component.css']
})
export class FeatureDetailComponent implements OnInit, OnDestroy, AfterViewInit {

  private _routerEventsSub: Subscription;

  name: string;
  heading: string;
  feature: Observable<Feature>;

  @ViewChild('snippetEditor') snippetEditor;
  @ViewChild('testSnippetEditor') testSnippetEditor;

  snippetEditorOptions: any = {
    highlightActiveLine: false,
    highlightGutterLine: false,
    showPrintMargin: false
  };

  testSnippetEditorOptions: any = {
    highlightActiveLine: false,
    highlightGutterLine: false,
    showPrintMargin: false
  };

  /**
   * go back using Location API
   * @returns {void} nothing
   */
  goBack() {

    // go back
    this.location.back();

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
   * after view init handler
   * @returns {void} nothing
   */
  ngAfterViewInit() {

    // disable automatic scrolling
    this.snippetEditor.$blockScrolling = Infinity;
    this.testSnippetEditor.$blockScrolling = Infinity;

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
