package Romario.controllers;

import Romario.controllers.dao.BookDAO;
import Romario.controllers.dao.PersonDAO;
import Romario.controllers.models.Book;
import Romario.controllers.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;

    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping()
    public String books(Model model) {
        model.addAttribute("book_list", bookDAO.getBooks());
        return "books/books";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") Book book) {
        bookDAO.add(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model,
                           @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookDAO.getBook(id));
        model.addAttribute("taken_person", personDAO.getPerson(bookDAO.getUser_id(id)));
        model.addAttribute("people", personDAO.getPeople());

        return "books/show";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookDAO.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookDAO.getBook(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") Book book) {
        bookDAO.update(id, book);
        return "redirect:/books/{id}";
    }

    @PutMapping("/{id}")
    public String freeBook(@PathVariable("id") int id) {
        bookDAO.freeBook(id);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}")
    public String takeBook(@PathVariable("id") int id,
                           @ModelAttribute("person") Person person) {
        bookDAO.takeBook(id, person.getUser_id());
        return "redirect:/books/{id}";
    }

}