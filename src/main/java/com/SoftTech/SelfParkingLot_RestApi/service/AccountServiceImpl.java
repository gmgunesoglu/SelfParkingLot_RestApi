package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.JWTAuthenticationFilter;
import com.SoftTech.SelfParkingLot_RestApi.security.JwtService;
import com.SoftTech.SelfParkingLot_RestApi.security.TokenQueue;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final PersonRepository personRepository;

    private final JWTAuthenticationFilter filter;
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

    //buradaki oluşturulan hata neden kullanıcıya dönmüyor ?
    @Override
    public JwtToken login(PersonLoginDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsernameOrEmail(),
                        dto.getPassword()
                )
        );
        Person person;// = personRepository.findByUsernameOrEmail(dto.getUsernameOrEmail(),dto.getUsernameOrEmail()).get();
        person = personRepository.findByUsernameOrEmail(dto.getUsernameOrEmail(),dto.getUsernameOrEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found: "+dto.getUsernameOrEmail()));
        String token=jwtService.generateToken(person);
        filter.add(token,person.getUsername());
        return new JwtToken(token);
    }

    @Override
    public HashMap<String, String> getTokens() {
        return filter.getTokens();
    }

    @Override
    public List<String> listQueue() {
        return filter.listQueue();
    }

    @Override
    public String logout(HttpServletRequest request) {
        Person person = getPersonFromRequest(request);

        return filter.logout(person.getUsername());
    }

    @Override
    public String changePassword(PersonChangePasswordDTO dto, HttpServletRequest request) {
        Person person = getPersonFromRequest(request);
        if(passwordEncoder.encode(dto.getOldPassword()).matches(person.getPassword())){
            // şifre doğru, güncelle
            person.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            personRepository.save(person);
            // sistemden çıkış yap
            filter.logout(person.getUsername());
            return "Şifreniz güncellendi, yeni şifrenizle giriş yapabilirsiniz.";
        }else{
            // şifre yanlış
            return "Şifre hatalı!";
        }
    }

    @Override
    public Person personUpdate(PersonUpdateDTO dto, HttpServletRequest request) {
        Person person = personRepository.findByEmail(dto.getEmail()).get();
        if(person==null){
            person=getPersonFromRequest(request);
            person.setEmail(dto.getEmail());
            person.setFirstName(dto.getFirstName());
            person.setLastName(dto.getLastName());
            person.setEmail(dto.getEmail());
            return personRepository.save(person);
        }else{
            throw new UsernameNotFoundException("Email kullanılıyor: "+dto.getEmail());
        }
    }

    private Person getPersonFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        String username = jwtService.extractUsername(token);
        return personRepository.findByUsername(username).get();
    }
}
