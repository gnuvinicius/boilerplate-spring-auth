package br.com.garage.notes.repositories;

import br.com.garage.notes.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {

    @Query(value = "SELECT b FROM Book b" +
            " WHERE b.tenant.id = :tenantId" +
            " ORDER BY b.atualizadoEm DESC")
    List<Book> buscarTodosBook(UUID tenantId);
}
