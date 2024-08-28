package br.com.garage.store.utils;

import br.com.garage.store.models.Compra;

@FunctionalInterface
public interface CompraPredicate {

    void execute(Compra compra);

}
