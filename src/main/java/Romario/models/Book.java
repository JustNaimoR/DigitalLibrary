package Romario.models;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Column(name = "name")
    private String name;

    @NotEmpty(message = "Name should not be empty")
    @Column(name = "author")
    private String author;

    @Min(value = 1500, message = "Year should be greater then 1500")
    @Column(name = "year")
    private int year;

    @Column(name = "taken")
    @Temporal(TemporalType.TIMESTAMP)
    private Date takenDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Person owner;

    public Book(int book_id, String name, String author, int year) {
        this.id = book_id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public Book() {}
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Getters and setters ~~~
    public Person getOwner() {
        return owner;
    }
    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Date getTakenDate() {
        return takenDate;
    }
    public void setTakenDate(Date takenDate) {
        this.takenDate = takenDate;
    }

    public boolean isDelayed() {
        return new Date().getTime() - takenDate.getTime() > TimeUnit.DAYS.toMillis(10);
    }
}