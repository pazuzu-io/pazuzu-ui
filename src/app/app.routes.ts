import { Routes } from '@angular/router';

import { FeatureListComponent } from './feature-list/feature-list.component';

export const APP_ROUTES: Routes = [
  {path: 'features/list', component: FeatureListComponent},
  {path: 'features', redirectTo: 'features/list'},
  {path: '**', redirectTo: 'features/list'}
];
