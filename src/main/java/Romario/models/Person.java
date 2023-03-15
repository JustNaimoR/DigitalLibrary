package Romario.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "FIO should not be empty")
    @Size(min = 10, max = 100, message = "FIO must be from 10 to 100 size!")
    @Column(name = "fio")
    private String FIO;

    @Min(value = 1900, message = "Birth year should be greater then 1900")
    @Column(name = "birthyear")
    private int birthYear;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {}

    public Person(String FIO, int birthYear) {
        this.FIO = FIO;
        this.birthYear = birthYear;
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getters and setters ~~~
    public List<Book> getBooks() {
        return books;
    }
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getFIO() {
        return FIO;
    }
    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public int getBirthYear() {
        return birthYear;
    }
    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}