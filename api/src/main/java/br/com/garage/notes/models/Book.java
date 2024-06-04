package br.com.garage.notes.models;

import br.com.garage.auth.models.Tenant;
import br.com.garage.commons.models.AggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "tb_books")
@Getter
public class Book extends AggregateRoot implements Serializable {

    private String titulo;

    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private List<Note> notes;
}
