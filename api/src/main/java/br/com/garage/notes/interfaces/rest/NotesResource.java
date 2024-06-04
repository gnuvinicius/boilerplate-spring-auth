package br.com.garage.notes.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notes")
@RestController
public class NotesResource {

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().build();
    }
}
