package br.com.garage.notes.interfaces.rest;

import br.com.garage.auth.config.security.UserAuthInfo;
import br.com.garage.notes.dtos.BookDTO;
import br.com.garage.notes.dtos.NoteDTO;
import br.com.garage.notes.models.Book;
import br.com.garage.notes.repositories.BookRepository;
import br.com.garage.notes.repositories.NoteRepository;
import jakarta.servlet.ServletContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/notes")
@RestController
public class NotesResource {

    @Autowired
    private ServletContext context;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<?> getAll() {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");

        var notes = noteRepository.buscaNotes(userInfo.getUsuarioId(), userInfo.getTenantId());
        return ResponseEntity.ok(notes);
    }

    @Transactional
    @PostMapping("books")
    public ResponseEntity<?> cadastraBook(@RequestBody BookDTO dto) {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");
        var book = new Book(dto.getTitulo(), dto.getDescricao(), userInfo.getTenantId(), userInfo.getUsuarioId());

        var entity = bookRepository.save(book);
        return ResponseEntity.ok().body(entity);
    }

    @GetMapping("books")
    public ResponseEntity<?> getBooks() {
        List<BookDTO> collect = bookRepository.findAll().stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(collect);
    }

    @PostMapping("{bookId}")
    public ResponseEntity<?> cadastraNovaNota(@RequestBody NoteDTO dto, @PathVariable UUID bookId) {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");

        return ResponseEntity.ok().build();
    }

}
