package br.com.garage.notes.models;

import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.commons.enums.EnumStatus;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    private String titulo;

    private String conteudo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", referencedColumnName = "id", nullable = false)
    private Book book;

    public Note(String titulo, String conteudo, Tenant tenant, Usuario usuario) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.tenant = tenant;
        this.usuario = usuario;
        this.status = EnumStatus.ATIVO;
    }
}
