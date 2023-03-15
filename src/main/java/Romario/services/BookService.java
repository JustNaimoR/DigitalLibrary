package Romario.services;

import Romario.models.Book;
import Romario.models.Person;
import Romario.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    public Person getOwner(int id) {
        Book book = bookRepository.findById(id).orElse(null);

        return book == null? null: book.getOwner();
    }

    public Book getByName(String name) {
        List<Book> list = bookRepository.findByName(name);

        return list.isEmpty()? null: list.get(0);
    }

    public List<Book> getByFirstLetters(String name) {
        return bookRepository.findByNameStartingWith(name);
    }

    public List<Book> getPage(int page, int itemsPerPage, boolean sort) {
        if (!sort) {
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.Direction.ASC, "year")).getContent();
        }
    }

//    public boolean isDelayed(int id) {
//        Optional<Book> optionalBook = bookRepository.findById(id);
//
//        if (optionalBook.isPresent()) {
//            Book book = optionalBook.get();
//
//            return new Date().getTime() - book.getTakenDate().getTime() >
//                    TimeUnit.DAYS.toMillis(10);
//        }
//        return false;
//    }

    @Transactional
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void updateBook(int id, Book book) {
        book.setId(id);

        bookRepository.save(book);
    }

    @Transactional
    public void releaseBook(int id) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            Person owner = book.getOwner();

            book.setOwner(null);
            book.setTakenDate(null);
            owner.getBooks().remove(book);
        }
    }

    @Transactional
    public void takeBook(int id, Person person) {
        Optional<Book> optionalBook = bookRepository.findById(id);

        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            // Дата взятия книги
            book.setTakenDate(new Date());

            book.setOwner(person);
            if (person.getBooks() != null) {
                person.getBooks().add(book);
            } else {
                person.setBooks(new ArrayList<>(Collections.singletonList(book)));
            }
        }
    }
}