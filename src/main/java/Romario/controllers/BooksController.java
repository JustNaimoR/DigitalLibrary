package Romario.controllers;

import Romario.models.Book;
import Romario.models.Person;
import Romario.services.BookService;
import Romario.services.PeopleService;
import Romario.util.BookValidator;
import net.bytebuddy.pool.TypePool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookService bookService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(BookService bookService, PeopleService peopleService, BookValidator bookValidator) {
        this.bookService = bookService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @GetMapping()
    public String books(@RequestParam("sort_by_year") Optional<Boolean> sortByYear,
                        @RequestParam("page") Optional<Integer> page,
                        @RequestParam("books_per_page") Optional<Integer> booksPerPage,
                        Model model) {
        List<Book> books = Collections.emptyList();
        boolean sort = sortByYear.isPresent() && sortByYear.get();

        if (!page.isPresent() && !booksPerPage.isPresent()) {
            // Все книги на странице
            books = bookService.getBooks();

            if (sort) {
                books.sort(Comparator.comparingInt(Book::getYear));
            }
        } else if (page.isPresent() && booksPerPage.isPresent()) {
            // Страницы книг
            books = bookService.getPage(page.get(), booksPerPage.get(), sort);

            model.addAttribute("page", page.get());
            model.addAttribute("books_per_pages", booksPerPage.get());
            model.addAttribute("hasPages", true);
        }

        model.addAttribute("book_list", books);
        return "books/books";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "books/new";

        bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model,
                           @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.getBook(id));
        model.addAttribute("taken_person", bookService.getOwner(id));
        model.addAttribute("people", peopleService.getPeople());

        return "books/show";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/new")
    public String newBook(Model model) {
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.getBook(id));
        return "books/edit";
    }

    @PatchMapping("/{id}/edit")
    public String updateBook(@PathVariable("id") int id,
                             @ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "books/edit";

        bookService.updateBook(id, book);
        return "redirect:/books/{id}";
    }

    @PutMapping("/{id}")
    public String freeBook(@PathVariable("id") int id) {
        bookService.releaseBook(id);
        return "redirect:/books/{id}";
    }

    @PatchMapping("/{id}")
    public String takeBook(@PathVariable("id") int id,
                           @ModelAttribute("person") Person person) {
        bookService.takeBook(id, person);
        return "redirect:/books/{id}";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("book_name") Book searchingBooks,
                         Model model) {
        // Проверка на начальную страницу без поиска на пустоту str

        if (searchingBooks.getName() == null) {
            model.addAttribute("searching_flag", false);
        } else {
            // Поиск по первым буквам названия
            model.addAttribute("found_books", bookService.getByFirstLetters(searchingBooks.getName()));
            model.addAttribute("searching_flag", true);
        }

        return "books/search";
    }
}