package Romario.services;

import Romario.models.Book;
import Romario.models.Person;
import Romario.repositories.PeopleRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> getPeople() {
        return peopleRepository.findAll();
    }

    public Person getPerson(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public List<Book> getTakenBooks(int id) {
        Person person = peopleRepository.findById(id).orElse(null);
        assert person != null;

        // Без инициализации, книги в списке не будут получены вне метода
        Hibernate.initialize(person.getBooks());
        return person.getBooks();
    }

    public Person findByFIO(String FIO) {
        List<Person> list = peopleRepository.findByFIO(FIO);

        return list.isEmpty()? null: list.get(0);
    }

    @Transactional
    public void addPerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void updatePerson(int id, Person person) {
        person.setId(id);

        peopleRepository.save(person);
    }
}