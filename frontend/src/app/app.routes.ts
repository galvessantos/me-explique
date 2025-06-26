import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home';
import { SettingsComponent } from './pages/settings/settings';
import { HelpComponent } from './pages/help/help';

export const appRoutes: Routes = [
  { path: '',         component: HomeComponent,        pathMatch: 'full' },
  { path: 'settings', component: SettingsComponent                 },
  { path: 'help',     component: HelpComponent                     },
  { path: '**',       redirectTo: ''                               }
];
