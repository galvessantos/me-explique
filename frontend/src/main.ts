// src/main.ts
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient }    from '@angular/common/http';
import { provideRouter }        from '@angular/router';
import {
  provideBrowserGlobalErrorListeners,
  provideZoneChangeDetection
} from '@angular/core';


import { AppComponent } from './app/app';
import { appRoutes }    from './app/app.routes';

bootstrapApplication(AppComponent, {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideHttpClient(),
    provideHttpClient(),
    provideRouter(appRoutes)
  ]
}).catch(err => console.error(err));
