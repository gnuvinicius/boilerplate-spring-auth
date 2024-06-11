package br.com.garage.notes.dtos;

import br.com.garage.commons.enums.EnumStatus;
import br.com.garage.notes.models.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@NoArgsConstructor
@Data
public class BookDTO {
    public UUID id;
    private String titulo;
    private String descricao;
    private List<NoteDTO> notes;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.titulo = book.getTitulo();
        this.descricao = book.getDescricao();
        this.criadoEm = book.getCriadoEm();
        this.atualizadoEm = book.getAtualizadoEm();
        this.notes = book.getNotes().stream()
                .filter(n -> n.getStatus().equals(EnumStatus.ATIVO))
                .map(NoteDTO::new).toList();
    }
}
