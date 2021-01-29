import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { FutbolSharedModule } from 'app/shared/shared.module';
import { FutbolCoreModule } from 'app/core/core.module';
import { FutbolAppRoutingModule } from './app-routing.module';
import { FutbolHomeModule } from './home/home.module';
import { FutbolEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    FutbolSharedModule,
    FutbolCoreModule,
    FutbolHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    FutbolEntityModule,
    FutbolAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class FutbolAppModule {}
