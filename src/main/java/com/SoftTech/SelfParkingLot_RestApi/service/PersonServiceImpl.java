package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Person get(Long id) {
        return personRepository.findById(id).get();
    }

    @Override
    public Person save(Person user) {
        return personRepository.save(user);
    }

    @Override
    public Person delete(Long id) {
        Person user = personRepository.findById(id).get();
        personRepository.delete(user);
        return user;
    }

    @Override
    public Person update(Person user, Long id) {
        return null;
//        Person updatedUser = personRepository.findById(id).get();
//        updatedUser.setAuthority(user.getAuthority());
//        updatedUser.setEmail(user.getEmail());
//        updatedUser.setUserName(user.getUserName());
//        updatedUser.setPassword(user.getPassword());
//        updatedUser.setPhoneNumber(user.getPhoneNumber());
//        updatedUser.setFirstName(user.getFirstName());
//        updatedUser.setLastName(user.getLastName());
////        updatedUser.setParkingLots(user.getParkingLots());
////        updatedUser.setSharedParkingLots(user.getSharedParkingLots());
//        return personRepository.save(updatedUser);
    }
}
