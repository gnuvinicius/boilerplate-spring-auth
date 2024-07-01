import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ProdutoComponent } from './pages/produto/produto.component';
import { VendasComponent } from './pages/vendas/vendas.component';
import { ClientesComponent } from './pages/clientes/clientes.component';
import { ProdutoFormComponent } from './pages/produto/produto-form/produto-form.component';
import { ConfigComponent } from './pages/config/config.component';

export const routes: Routes = [
  {
    path: 'home',
    component: HomeComponent,
  },
  {
    path: 'produtos',
    component: ProdutoComponent,
  },
  {
    path: 'produtos/form/:id',
    component: ProdutoFormComponent,
  },
  {
    path: 'produtos/form',
    component: ProdutoFormComponent,
  },
  {
    path: 'vendas',
    component: VendasComponent,
  },
  {
    path: 'clientes',
    component: ClientesComponent,
  },
  {
    path: 'config',
    component: ConfigComponent,
  },
];
