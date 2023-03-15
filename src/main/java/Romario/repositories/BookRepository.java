package Romario.repositories;

import Romario.models.Book;
import Romario.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByName(String name);
    List<Book> findByNameStartingWith(String name);
}
