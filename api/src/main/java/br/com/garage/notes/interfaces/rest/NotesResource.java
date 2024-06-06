package br.com.garage.notes.interfaces.rest;

import br.com.garage.auth.config.security.UserAuthInfo;
import br.com.garage.notes.dtos.NoteDTO;
import br.com.garage.notes.repositories.NoteRepository;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/notes")
@RestController
public class NotesResource {

    @Autowired
    private ServletContext context;

    @Autowired
    private NoteRepository noteRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");

        var notes = noteRepository.buscaNotes(userInfo.getUsuarioId(), userInfo.getTenantId());
        return ResponseEntity.ok(notes);
    }

    @PostMapping("{bookId}")
    public ResponseEntity<?> cadastraNovaNota(@RequestBody NoteDTO dto, @PathVariable UUID bookId) {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");

        return ResponseEntity.ok().build();
    }
}
