export type Produto = {
  id?: number;
  titulo?: string | null;
  descricao?: string | null;
  valor: number;
  quantidade?: number;
  categorias: any;
  atualizadoEm?: Date;
  status?: boolean;
  anexos?: any | null;
};