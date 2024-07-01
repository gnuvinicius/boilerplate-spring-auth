import { inject } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";

export default class BaseComponent {
  route = inject(ActivatedRoute);
  router = inject(Router);
  
  navigateTo(route: string) {
    this.router.navigate([route]);
  }
}
