package br.com.garage.notes.models;

import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.models.AggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_books")
@Getter
@NoArgsConstructor
public class Book extends AggregateRoot implements Serializable {

    private String titulo;

    private String descricao;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
    private final List<Note> notes = new ArrayList<>();

    public Book(String titulo, String descricao, Tenant tenant, Usuario usuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.tenant = tenant;
        this.usuario = usuario;
        this.status = EnumStatus.ATIVO;
    }

    public void adicionaNote(Note note) {
        this.notes.add(note);
    }


}
