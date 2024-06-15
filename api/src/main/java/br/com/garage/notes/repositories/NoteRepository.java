package br.com.garage.notes.repositories;

import br.com.garage.notes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface NoteRepository extends JpaRepository<Note, UUID> {

    @Query(value = "SELECT n FROM Note n WHERE n.usuario.id = :usuarioId AND n.id = :noteId")
    Optional<Note> buscaNotePorIdAndUsuarioId(UUID noteId, Long usuarioId);
}
