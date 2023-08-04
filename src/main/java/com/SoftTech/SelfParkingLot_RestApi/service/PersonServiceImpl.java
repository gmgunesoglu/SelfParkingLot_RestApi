package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.AuthenticationResponseDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Authority;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


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

    @Override
    public Person showPersonInfo(HttpServletRequest request) {
        return getPersonFromRequest(request);
    }

    @Override
    public Person register(PersonDTO dto) {
        Person person = new Person();
        person.setUsername(dto.getUserName());
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAuthority(dto.getAuthority());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setEmail(dto.getEmail());
        return personRepository.save(person);
    }

    @Override
    public JwtToken login(PersonLoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );
        Person person = personRepository.findByEmail(dto.getEmail()).get();
        return new JwtToken(jwtService.generateToken(person));
    }

    private Person getPersonFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        String username = jwtService.extractUsername(token);
        return personRepository.findPersonByUsername(username).get();
    }
}
