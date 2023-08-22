package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.CurrentTokens;
import com.SoftTech.SelfParkingLot_RestApi.security.JWTAuthenticationFilter;
import com.SoftTech.SelfParkingLot_RestApi.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService{

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private CurrentTokens currentTokens;

    @Autowired
    public AccountServiceImpl(PersonRepository personRepository, JWTAuthenticationFilter filter, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        currentTokens=filter.getCurrentTokens();
    }

    @Override
    public Person showPersonInfo(HttpServletRequest request) {
        return getPersonFromRequest(request);
    }

    @Override
    public Person register(PersonDTO dto) {
        if(personRepository.checkWithUsernameOrEmail(dto.getUserName(),dto.getEmail())!=null){
            throw new GlobalRuntimeException("Username or email is already used!",HttpStatus.BAD_REQUEST);
        }

        Person person = new Person();
        person.setUsername(dto.getUserName());
        person.setPassword(passwordEncoder.encode(dto.getPassword()));
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setAuthority(dto.getAuthority());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setEmail(dto.getEmail());
        person.setEnable(true);
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
        if(!person.isEnable()){
            throw new GlobalRuntimeException("This user is removed.", HttpStatus.NOT_FOUND);
        }
        if(person.getBlockTime()!=null){
            //blocklanmış ise...
            if(person.getBlockTime().before(new Date(System.currentTimeMillis()-86400000))){
                //block süresi geçti ise null yapıp login ol
                person.setBlockTime(null);
                personRepository.save(person);
            }else{
                // block süresi geçmedi ise login olma
                // Hatta broda ip blocklamak gerekir. Tüm bu işlemler kötü niyetli kullanıcılara karşı sunucuyu savunma cabaları.
                // ama kesin bir koruma yok, sadece yavaşlatma var. İp blocklama ile sunucu performansı çok iyi bir sekilde korunabilir

                throw new GlobalRuntimeException("This user is blocked for 24 Hours: "+person.getUsername(), HttpStatus.NOT_FOUND);
            }
        }
        String token=jwtService.generateToken(person);
        try{
            currentTokens.addToCache(token,person.getUsername());
        }catch (AuthenticationException ex){
            person.setBlockTime(new Date(System.currentTimeMillis()));
            personRepository.save(person);
            throw ex;
        }
        return new JwtToken(token);
    }

    @Override
    public HashMap<String, String> getTokens() {
        return currentTokens.getTokens();
    }

    @Override
    public List<String> listQueue() {
        return currentTokens.listQueue();
    }

    @Override
    public String logout(HttpServletRequest request) {
        Person person = getPersonFromRequest(request);

        return currentTokens.remove(person.getUsername());
    }

    @Override
    public String changePassword(PersonChangePasswordDTO dto, HttpServletRequest request) {
        Person person = getPersonFromRequest(request);
        if(passwordEncoder.matches(dto.getOldPassword(),person.getPassword())){
            // şifre doğru, güncelle
            person.setPassword(passwordEncoder.encode(dto.getNewPassword()));
            personRepository.save(person);
            // sistemden çıkış yap
            currentTokens.remove(person.getUsername());
            return "Password changed, you can login now.";
        }else{
            // şifre yanlış
            return "Password is incorrect!";
        }
    }

    @Override
    public Person personUpdate(PersonUpdateDTO dto, HttpServletRequest request) {
        Person person = personRepository.getPersonByEmail(dto.getEmail());
        if(person!=null){
            // email kullanılıyor.
            person = getPersonFromRequest(request);
            if(!person.getEmail().equals(dto.getEmail())){
                throw new UsernameNotFoundException("Email is already uses! "+dto.getEmail());
            }
        }else{
            person = getPersonFromRequest(request);
        }
        person.setEmail(dto.getEmail());
        person.setFirstName(dto.getFirstName());
        person.setLastName(dto.getLastName());
        person.setPhoneNumber(dto.getPhoneNumber());
        person.setSecretKey(dto.getSecretKey());
        return personRepository.save(person);
    }

    @Override
    public String disable(HttpServletRequest request, PersonLoginDTO dto) {
        //kullanıcıyı çek
        Person person = getPersonFromRequest(request);
        if(!person.getUsername().equals(dto.getUsernameOrEmail())&&person.getEmail().equals(dto.getUsernameOrEmail())){
            // username/email yanlış ise...
            new UsernameNotFoundException("Username or Password is not corrected");
        }
        if(!passwordEncoder.matches(dto.getPassword(),person.getPassword())){
            return "Username or Password is not corrected";
        }
        person.setEnable(false);
        currentTokens.remove(person.getUsername());
        personRepository.save(person);
        return "Account removed!";
    }

    private Person getPersonFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        String username = jwtService.extractUsername(token);
        return personRepository.findByUsername(username).get();
    }
}
