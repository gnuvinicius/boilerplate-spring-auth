import { CommonModule } from '@angular/common';
import { Component, OnInit, inject } from '@angular/core';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { InputTextModule } from 'primeng/inputtext';
import { TableModule } from 'primeng/table';
import { Produto } from '../../models/produto';
import { RouterModule } from '@angular/router';
import BaseComponent from '../../baseComponent';
import { ProdutoService } from './produto.service';

@Component({
  selector: 'app-produto',
  standalone: true,
  imports: [
    CommonModule,
    InputTextModule,
    TableModule,
    CardModule,
    ButtonModule,
    RouterModule,
  ],
  templateUrl: './produto.component.html',
  styleUrl: './produto.component.scss',
})
export class ProdutoComponent extends BaseComponent implements OnInit {
  produtos: Produto[] = [];
  service = inject(ProdutoService);

  ngOnInit(): void {
    this.service
      .getProdutos()
      .subscribe((response: Produto[]) => (this.produtos = response));
  }

  editar(produto: Produto) {
    this.navigateTo(`/produtos/form/${produto.id}`);
  }

  excluir(produto: any) {
    console.log(produto);
  }
}
