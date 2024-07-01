import { Injectable } from "@angular/core";
import { Produto } from "../../models/produto";
import { of } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ProdutoService {

  products: Produto[] = [
    {
      id: 2,
      titulo: 'John Pope',
      descricao: `Lorem Ipsum is simply dummy text of the printing and typesetting industry.
        Lorem Ipsum has been the industrys standard dummy text ever since the 1500s,
        when an unknown printer took a galley of type and scrambled it to make`,
      valor: 56,
      categorias: [],
      quantidade: 10,
      atualizadoEm: new Date(),
      status: false,
    },
  ];

  getProdutos() {
    return of(this.products);
  }
}