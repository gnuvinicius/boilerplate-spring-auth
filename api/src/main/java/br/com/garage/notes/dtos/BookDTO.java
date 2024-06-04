package br.com.garage.notes.dtos;

import lombok.Data;

import java.util.List;

@Data
public class BookDTO {
    private String titulo;
    private String descricao;
    private List<NoteDTO> notes;
}
