package br.com.garage.notes.interfaces.rest;

import br.com.garage.auth.config.security.UserAuthInfo;
import br.com.garage.commons.utils.Utils;
import br.com.garage.notes.dtos.BookDTO;
import br.com.garage.notes.dtos.NoteDTO;
import br.com.garage.notes.models.Book;
import br.com.garage.notes.models.Note;
import br.com.garage.notes.repositories.BookRepository;
import br.com.garage.notes.repositories.NoteRepository;
import jakarta.servlet.ServletContext;
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
    private ServletContext context;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private BookRepository bookRepository;

    @Transactional
    @PostMapping("books")
    public ResponseEntity<?> cadastraBook(@RequestBody BookDTO dto) throws URISyntaxException {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");
        var book = new Book(dto.getTitulo(), dto.getDescricao(), userInfo.getTenantId(), userInfo.getUsuarioId());

        var entity = bookRepository.save(book);
        URI uri = new URI("/books?id=" + entity.getId().toString());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("books")
    public List<BookDTO> getBooks() {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");
        return bookRepository.buscarTodosBook(userInfo.getTenantId()).stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> cadastraNovaNota(@RequestBody NoteDTO dto, @PathVariable UUID bookId) throws URISyntaxException {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");
        Book book = Utils.requireNotEmpty(bookRepository.findById(bookId));
        Note note = new Note(dto.getTitulo(), dto.getConteudo(), userInfo.getUsuarioId(), userInfo.getTenantId(), book);
        noteRepository.save(note);
        return ResponseEntity.created(new URI(note.getId().toString())).build();
    }

    @Transactional
    @PutMapping("/{noteId}")
    public ResponseEntity<?> atualizaNote(@RequestBody NoteDTO dto, @PathVariable UUID noteId) {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");
        Note note = Utils.requireNotEmpty(noteRepository.buscaNotePorIdAndUsuarioId(noteId, userInfo.getUsuarioId()));

        note.atualiza(dto);
        noteRepository.save(note);
        return ResponseEntity.ok().build();
    }

    @Transactional
    @DeleteMapping("/{noteId}")
    public ResponseEntity<?> removeNote(@PathVariable UUID noteId) {
        UserAuthInfo userInfo = (UserAuthInfo) context.getAttribute("userInfo");
        Note note = Utils.requireNotEmpty(noteRepository.buscaNotePorIdAndUsuarioId(noteId, userInfo.getUsuarioId()));

        note.desativa();
        noteRepository.save(note);
        return ResponseEntity.ok().build();
    }

}
