package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.JwtToken;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PersonLoginDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
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
