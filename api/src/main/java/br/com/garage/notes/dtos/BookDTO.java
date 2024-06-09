package br.com.garage.notes.dtos;

import br.com.garage.notes.models.Book;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class BookDTO {
    public UUID id;
    private String titulo;
    private String descricao;
    private List<NoteDTO> notes;
    private LocalDateTime criadoEm;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.titulo = book.getTitulo();
        this.descricao = book.getDescricao();
        this.criadoEm = book.getCriadoEm();
        this.notes = book.getNotes().stream().map(NoteDTO::new).toList();
    }
}
