import { Routes } from '@angular/router';

import { FeatureListComponent } from './feature-list/feature-list.component';
import { FeatureDetailComponent } from './feature-detail/feature-detail.component';

export const APP_ROUTES: Routes = [
  {path: 'features/list', component: FeatureListComponent},
  {path: 'features/detail/:id', component: FeatureDetailComponent},
  {path: 'features', redirectTo: 'features/list'},
  {path: '**', redirectTo: 'features/list'}
];
