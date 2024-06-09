package br.com.garage.notes.repositories;

import br.com.garage.notes.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID> {


}
