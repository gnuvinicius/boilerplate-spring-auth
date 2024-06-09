package br.com.garage.notes.dtos;

import br.com.garage.notes.models.Note;
import lombok.Data;

@Data
public class NoteDTO {
    private String titulo;
    private String conteudo;

    public NoteDTO(Note note) {
        this.titulo = note.getTitulo();
        this.conteudo = note.getConteudo();
    }
}
