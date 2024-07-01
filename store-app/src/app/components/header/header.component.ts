import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { RouterModule } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { MenuModule } from 'primeng/menu';
import BaseComponent from '../../baseComponent';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule, MenuModule, ButtonModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss',
})
export class HeaderComponent extends BaseComponent {
  controlItens: MenuItem[] = [
    {
      items: [
        {
          label: 'Configurações',
          icon: 'pi pi-refresh',
          command: () => {
            this.navigateTo('/config');
          },
        },
        {
          label: 'Logout',
          icon: 'pi pi-upload',
          command: () => {
            this.navigateTo('/logout');
          },
        },
      ],
    },
  ];


}
