package br.com.garage.notes.models;

import br.com.garage.auth.models.Tenant;
import br.com.garage.auth.models.Usuario;
import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.commons.models.AggregateRoot;
import br.com.garage.notes.dtos.NoteDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

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

    public Note(String titulo, String conteudo, UUID usuarioId, UUID tenantId, Book book) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.usuario = new Usuario(usuarioId);
        this.tenant = new Tenant(tenantId);
        this.status = EnumStatus.ATIVO;
        this.book = book;
    }

    public void atualiza(NoteDTO dto) {
        this.titulo = dto.getTitulo();
        this.conteudo = dto.getConteudo();
        this.atualizadoEm = LocalDateTime.now();
    }

    public void desativa() {
        this.status = EnumStatus.INATIVO;
        this.atualizadoEm = LocalDateTime.now();
    }
}
