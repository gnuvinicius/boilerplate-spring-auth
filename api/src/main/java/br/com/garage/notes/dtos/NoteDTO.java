package br.com.garage.notes.dtos;

import br.com.garage.notes.models.Note;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class NoteDTO {
    private UUID id;
    private String titulo;
    private String conteudo;
    private LocalDateTime criadoEm;
    private LocalDateTime atualizadoEm;

    public NoteDTO(Note note) {
        this.id = note.getId();
        this.titulo = note.getTitulo();
        this.conteudo = note.getConteudo();
        this.criadoEm = note.getCriadoEm();
        this.atualizadoEm = note.getAtualizadoEm();
    }
}
