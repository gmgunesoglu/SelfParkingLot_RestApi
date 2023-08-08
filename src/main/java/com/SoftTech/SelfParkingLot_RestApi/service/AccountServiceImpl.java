package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonInfoDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.service.JwtServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final PersonRepository personRepository;
    private final JwtServiceImpl jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Person findByUserName(String userName) {
        return personRepository.findByUserName(userName).get();
    }

    @Override
    public Person findByEmail(String email) {
        return personRepository.findByEmail(email).get();
    }

    @Override
    public Person findByUserNameOrEmail(String userNameOrEmail, String emailOrUserName) {
        return personRepository.findByUserNameOrEmail(userNameOrEmail,emailOrUserName).get();
    }

    @Override
    public JwtToken login(PersonLoginDTO dto) {
        Person person = personRepository.findByUserName(dto.getUserName()).get();
        if(passwordEncoder.matches(dto.getPassword(),person.getPassword())){
            String token = jwtService.generateToken(person);
            return new JwtToken(token);
        }
        return null;
    }

    @Override
    public Person register(PersonDTO dto) {
        Person person = new Person();
        person.setUserName(dto.getUserName());
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAuthority(dto.getAuthority());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setEmail(dto.getEmail());
        return personRepository.save(person);
    }

    @Override
    public PersonInfoDTO showPersonInfo(HttpServletRequest request) {
        Person person = getPersonFromRequest(request);
        PersonInfoDTO dto = new PersonInfoDTO();
        dto.setAuthority(person.getAuthority());
        dto.setEmail(person.getEmail());
        dto.setPhoneNumber(person.getPhoneNumber());
        dto.setUserName(person.getUserName());
        dto.setLastName(person.getLastName());
        dto.setFirstName(person.getFirstName());
        return dto;
    }

    private Person getPersonFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        String username = jwtService.extractUserName(token);
        Person person = personRepository.findByUserName(username).get();
        return person;
    }
}
