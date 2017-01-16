import { Title } from '@angular/platform-browser';

import { EventBusService } from './event-bus.service';
import { FeatureService } from './feature.service';

export const APP_PROVIDERS = [
  Title,
  EventBusService,
  FeatureService
];
