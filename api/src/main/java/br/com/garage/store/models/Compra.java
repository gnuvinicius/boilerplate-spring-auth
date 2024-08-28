package br.com.garage.store.models;

import br.com.garage.store.utils.CompraPredicate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

@Builder
@Getter
public class Compra {

    private Long id;
    private LocalDate dataCompra;

    public void executaCompra(CompraPredicate predicate) {
        predicate.execute(this);
    }

    public void pertenceAPessoa(List<Compra> compras, Consumer<Compra> consumer) {
        for (Compra compra : compras) {
            consumer.accept(compra);
        }
    }
}
