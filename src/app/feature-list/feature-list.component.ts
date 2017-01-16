import { Component, OnInit } from '@angular/core';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';
import { Feature } from '../models/feature';
import { FeatureService } from '../feature.service';

@Component({
  selector: 'app-feature-list',
  templateUrl: './feature-list.component.html',
  styleUrls: ['./feature-list.component.css']
})
export class FeatureListComponent implements OnInit {

  heading: string;
  features: Array<Feature>;

  constructor(
    private eventBusService: EventBusService,
    private featureService: FeatureService
  ) { }

  ngOnInit() {
    this.heading = 'Feature list';
    this.eventBusService.emit(APP_TITLE_CHANGE, this.heading);
    this.features = this.featureService.getFeatures();
  }

}
