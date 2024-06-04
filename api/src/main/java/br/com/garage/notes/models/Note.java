package br.com.garage.notes.models;

import br.com.garage.auth.models.Tenant;
import br.com.garage.commons.models.AggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "tb_notes")
@NoArgsConstructor
@Getter
public class Note extends AggregateRoot implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", referencedColumnName = "id", nullable = false)
    private Tenant tenant;

    private String titulo;
    private String conteudo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    public Note(String titulo, String conteudo) {
        this.titulo = titulo;
        this.conteudo = conteudo;
    }
}
