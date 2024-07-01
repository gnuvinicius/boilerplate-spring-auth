import { Component, OnInit, inject } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { InputTextareaModule } from 'primeng/inputtextarea';
import { MultiSelectModule } from 'primeng/multiselect';
import { TableModule } from 'primeng/table';
import { Categoria } from '../../../models/categoria';
import BaseComponent from '../../../baseComponent';
import { FileUploadModule } from 'primeng/fileupload';

import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Produto } from '../../../models/produto';

@Component({
  selector: 'app-produto-form',
  standalone: true,
  imports: [
    CardModule,
    InputTextModule,
    InputTextareaModule,
    MultiSelectModule,
    ButtonModule,
    RouterModule,
    DialogModule,
    TableModule,
    ReactiveFormsModule,
    FileUploadModule,
  ],
  templateUrl: './produto-form.component.html',
  styleUrl: './produto-form.component.scss',
})
export class ProdutoFormComponent extends BaseComponent implements OnInit {
  categorias!: Categoria[];
  visible: boolean = false;
  selectedCategorias!: any[];

  productForm = new FormGroup({
    titulo: new FormControl('', [Validators.required]),
    descricao: new FormControl(''),
    valor: new FormControl(''),
    categorias: new FormControl(''),
    anexos: new FormControl(''),
  });

  ngOnInit() {
    const param = this.route.snapshot.params['id'];
    if (param) {
      console.log('tem o parametro:', param);
    }

    this.categorias = [
      { name: 'T-shirt', code: 'NY' },
      { name: 'T-Shirt oversized', code: 'RM' },
      { name: 'Bermuda', code: 'LDN' },
      { name: 'Boné', code: 'IST' },
    ];
  }

  salvar() {
    const produto = this.handleLoadFormToSubmit();
    console.log(produto);
    // this.router.navigate(['/produtos']);
  }

  showDialog() {
    this.visible = true;
  }

  excluirCategoria(categoria: Categoria) {
    console.log(categoria);
  }

  private handleLoadFormToSubmit(): Produto {
    return {
      titulo: this.productForm.controls['titulo'].value,
      descricao: this.productForm.controls['descricao'].value,
      valor: Number(this.productForm.controls['valor'].value),
      categorias: this.productForm.controls['categorias'].value,
    };
  }

  detectFiles(event: any) {
    let files = event.target.files;
    console.log(files);
    // for (let file of files) {
    //   let reader = new FileReader();
    //   reader.onload = (e: any) => {
    //       this.photos.push(this.createItem({
    //           file,
    //           url: e.target.result  //Base64 string for preview image
    //       }));
    //   }
    //   reader.readAsDataURL(file);
  }
}
