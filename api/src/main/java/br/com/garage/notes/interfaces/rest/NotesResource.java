package br.com.garage.notes.interfaces.rest;

import br.com.garage.notes.dtos.NoteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/notes")
@RestController
public class NotesResource {

    @PostMapping("{bookId}")
    public ResponseEntity<?> cadastraNovaNota(@RequestBody NoteDTO dto, @PathVariable UUID bookId) {

        return ResponseEntity.ok().build();
    }
}
