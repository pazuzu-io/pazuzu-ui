import { Component, OnInit } from '@angular/core';

import { EventBusService, APP_TITLE_CHANGE } from '../event-bus.service';

@Component({
  selector: 'app-feature-list',
  templateUrl: './feature-list.component.html',
  styleUrls: ['./feature-list.component.css']
})
export class FeatureListComponent implements OnInit {

  constructor(
    private eventBusService: EventBusService
  ) { }

  ngOnInit() {
    this.eventBusService.emit(APP_TITLE_CHANGE, 'Feature List');
  }

}
