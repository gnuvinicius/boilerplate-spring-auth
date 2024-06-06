package br.com.garage.notes.interfaces.rest;

import br.com.garage.notes.dtos.NoteDTO;
import br.com.garage.notes.repositories.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/notes")
@RestController
public class NotesResource {

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        var notes = noteRepository.findAll();
        return ResponseEntity.ok(notes);
    }

    @PostMapping("{bookId}")
    public ResponseEntity<?> cadastraNovaNota(@RequestBody NoteDTO dto, @PathVariable UUID bookId) {
        return ResponseEntity.ok().build();
    }
}
