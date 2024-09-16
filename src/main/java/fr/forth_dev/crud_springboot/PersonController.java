package fr.forth_dev.crud_springboot;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {
    final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPersons() {
        return new ResponseEntity<>(personRepository.findAll(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person personCreated = personRepository.save(person);

        if( person.getName().length() <= 2) {
            throw new IllegalArgumentException("Name is too short");
        }
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                  .orElseThrow(() -> new PersonNotFoundException("Person not found!"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personUpdate) {
        Optional<Person> person = personRepository.findById(id);
        if(person.isEmpty()) throw new PersonNotFoundException("Person not found!");

        Person personFound = person.get();
        personFound.setCity(personUpdate.getCity());
        personFound.setPhoneNumber(personUpdate.getPhoneNumber());

        Person personUpdated = personRepository.save(personFound);
        return new ResponseEntity<>(personUpdated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        if(person.isEmpty()) throw new PersonNotFoundException("Person not found!");

        personRepository.delete(person.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
