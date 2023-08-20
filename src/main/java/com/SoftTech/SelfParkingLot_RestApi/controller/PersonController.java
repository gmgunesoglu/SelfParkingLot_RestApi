package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @GetMapping
    public List<Person> getPersons(){
        return personService.getAll();
    }

    @GetMapping("/{Id}")
    public Person getPerson(@PathVariable Long id){
        return personService.get(id);
    }

    @PostMapping
    public Person savePerson(@RequestBody Person user){
        return personService.save(user);
    }

    @DeleteMapping("/{id}")
    public Person deletePerson(@PathVariable Long id){
        return personService.delete(id);
    }

    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Long id){
        return personService.delete(id);
    }
}
