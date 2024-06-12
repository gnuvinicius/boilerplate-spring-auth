package br.com.garage.notes.interfaces.rest;

import br.com.garage.auth.config.security.UserAuthInfo;
import br.com.garage.commons.utils.Utils;
import br.com.garage.notes.config.userInfo.UserInfo;
import br.com.garage.notes.dtos.BookDTO;
import br.com.garage.notes.dtos.NoteDTO;
import br.com.garage.notes.models.Book;
import br.com.garage.notes.models.Note;
import br.com.garage.notes.repositories.BookRepository;
import br.com.garage.notes.repositories.NoteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequestMapping("/notes")
@RestController
public class NotesResource {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    @PostMapping("books")
    public ResponseEntity<?> cadastraBook(@UserInfo UserAuthInfo userInfo, @RequestBody BookDTO dto) throws URISyntaxException {
        var book = dto.toModel(userInfo);
        var entity = bookRepository.save(book);
        URI uri = new URI("/books?id=" + entity.getId().toString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("books")
    public List<BookDTO> getBooks(@UserInfo UserAuthInfo userInfo) {
        return bookRepository.buscarTodosBook(userInfo.getTenantId()).stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> cadastraNovaNota(@UserInfo UserAuthInfo userInfo, @RequestBody NoteDTO dto, @PathVariable UUID bookId) throws URISyntaxException {
        Book book = Utils.requireNotEmpty(bookRepository.findById(bookId));
        Note note = new Note(dto.getTitulo(), dto.getConteudo(), userInfo.getUsuarioId(), userInfo.getTenantId(), book);
        noteRepository.save(note);
        return ResponseEntity.created(new URI(note.getId().toString())).build();
    }

    @Transactional
    @PutMapping("/{noteId}")
    public ResponseEntity<?> atualizaNote(@UserInfo UserAuthInfo userInfo, @RequestBody NoteDTO dto, @PathVariable UUID noteId) {
        Note note = Utils.requireNotEmpty(noteRepository.buscaNotePorIdAndUsuarioId(noteId, userInfo.getUsuarioId()));

        note.atualiza(dto);
        noteRepository.save(note);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> removeNote(@UserInfo UserAuthInfo userInfo, @PathVariable UUID noteId) {
        Note note = Utils.requireNotEmpty(noteRepository.buscaNotePorIdAndUsuarioId(noteId, userInfo.getUsuarioId()));

        note.desativa();
        noteRepository.save(note);
        return ResponseEntity.ok().build();
    }

}
