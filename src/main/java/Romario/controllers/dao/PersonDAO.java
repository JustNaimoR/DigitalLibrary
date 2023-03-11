package Romario.controllers.dao;

import Romario.controllers.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Получить всех людей
    public List<Person> getPeople() {
        return jdbcTemplate.query("SELECT * FROM person", new BeanPropertyRowMapper<>(Person.class));
    }

    // Получить человека по id
    public Person getPerson(int user_id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE user_id=?", new Object[]{user_id},
                new BeanPropertyRowMapper<>(Person.class)).stream().findAny().orElse(null);
    }

    public void add(Person person) {
        jdbcTemplate.update("INSERT INTO person(name, surname, patronymic, birthyear) values (?, ?, ?, ?)",
                person.getName(), person.getSurname(), person.getPatronymic(), person.getBirthYear());
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE person SET name=?, surname=?, patronymic=?, birthyear=? WHERE user_id=?",
                person.getName(), person.getSurname(), person.getPatronymic(), person.getBirthYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE user_id=?", id);
    }

    public List<Book> getTakenBooks(int id) {
        return jdbcTemplate.query("SELECT * FROM book WHERE user_id=?", new Object[]{id}, new BeanPropertyRowMapper<>(Book.class));
    }

}