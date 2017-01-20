import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';
import { RouterModule } from '@angular/router';

import 'hammerjs';

import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/map';

import { APP_PROVIDERS } from './app.providers';
import { APP_ROUTES } from './app.routes';

import { AppComponent } from './app.component';
import { FeatureListComponent } from './feature-list/feature-list.component';
import { FeatureDetailComponent } from './feature-detail/feature-detail.component';
import { AceEditorDirective } from './ace-editor.directive';
import { FeatureCreateComponent } from './feature-create/feature-create.component';

@NgModule({
  declarations: [
    AppComponent,
    FeatureListComponent,
    FeatureDetailComponent,
    AceEditorDirective,
    FeatureCreateComponent
  ],
  imports: [
    BrowserModule,
    RouterModule.forRoot(APP_ROUTES),
    FormsModule,
    HttpModule
  ],
  providers: APP_PROVIDERS,
  bootstrap: [AppComponent]
})
export class PazuzuUIAppModule { }
