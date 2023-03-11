package Romario.controllers.dao;

import Romario.controllers.models.Book;
import Romario.controllers.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.List;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> getBooks() {
        return jdbcTemplate.query("SELECT * FROM book", new BeanPropertyRowMapper<>(Book.class));
    }

    public Book getBook(int book_id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE book_id=?", new Object[]{book_id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findAny().orElse(null);
    }

    public void add(Book book) {
        jdbcTemplate.update("INSERT INTO book(name, author, year) values (?, ?, ?)",
                book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET name=?, author=?, year=? WHERE book_id=?",
                book.getName(), book.getAuthor(), book.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id=?", id);
    }

    public int getUser_id(int book_id) {
        String sql = "SELECT * FROM book WHERE book_id=?";
        try {
            PreparedStatement ps = jdbcTemplate.getDataSource().getConnection().prepareStatement(sql);
            ps.setInt(1, book_id);

            ResultSet rs = ps.executeQuery();
            rs.next();  // ОБЯЗАТЕЛЬНО, ЧТОБЫ ПОЛУЧИТЬ ПЕРВЫЙ ЭЛЕМЕНТ ИЗ СПИСКА ОТВЕТА

            return rs.getInt("user_id");
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public void freeBook(int book_id) {
        jdbcTemplate.update("UPDATE book SET user_id=null WHERE book_id=?", book_id);
    }

    public void takeBook(int book_id, int user_id) {
        jdbcTemplate.update("UPDATE book SET user_id=? WHERE book_id=?", user_id, book_id);
    }

    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LibraryDB",
                "postgres", "postgres");

        PreparedStatement ps = conn.prepareStatement("SELECT * FROM book WHERE book_id=?");
        ps.setInt(1, 12);

        ResultSet resultSet = ps.executeQuery();
        resultSet.next();

        System.out.println(resultSet.getInt("book_id"));
        System.out.println(resultSet.getString("name"));
        System.out.println(resultSet.getString("author"));
        System.out.println(resultSet.getInt("year"));
        System.out.println(resultSet.getInt("user_id"));
    }

}