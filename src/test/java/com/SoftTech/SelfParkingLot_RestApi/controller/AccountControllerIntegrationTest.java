package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.Authority;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // jwt login olduktan sonra diğer metotlar için de kalıcı olmalı
public class AccountControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;  //request sender gibi veya psotman gibi

    @Autowired
    private AccountService accountService;  // throw exception lar için

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtLeatest;
    private String jwtOld;

    @Test
    @DisplayName("Person can be created")
    @Order(1)
    void registerTest_whenInputsOk_returnsPerson() throws JsonProcessingException {

        // Arrange
        PersonDTO requestDto = new PersonDTO();
        requestDto.setUserName("emre123");
        requestDto.setPassword("123qweqwe");
        requestDto.setFirstName("Emre");
        requestDto.setLastName("Tantu");
        requestDto.setEmail("emretantu@gmail.com");
        requestDto.setPhoneNumber("538 042 0000");
        requestDto.setAuthority(Authority.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.postForEntity("/account/register",request, Person.class);
        Person person = response.getBody();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be OK");
        Assertions.assertNotNull(
                person.getId(),
                "Person id should not be null");
        Assertions.assertNull(
                person.getSecretKey(),
                "Person secret key should be null");
        Assertions.assertNotEquals(
                requestDto.getPassword(),
                person.getPassword(),
                "person password should be encoded");
        Assertions.assertTrue(
                passwordEncoder.matches(requestDto.getPassword(),
                person.getPassword()),
                "encoded password should be match");
    }

    @Test
    @DisplayName("Username or Email have to be uniqe")
    @Order(2)
    void registerTest_whenUserNameOrEmailExists_throwsGlobalRuntimeException() throws JsonProcessingException {

        // Arrange
        PersonDTO requestDto = new PersonDTO();
        requestDto.setUserName("emre123_re");
        requestDto.setPassword("123qweqwe");
        requestDto.setFirstName("Emre");
        requestDto.setLastName("Tantu");
        requestDto.setEmail("emretantu@gmail.com");
        requestDto.setPhoneNumber("538 042 0000");
        requestDto.setAuthority(Authority.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.postForEntity("/account/register",request, Person.class);

        // Assert
        Assertions.assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode(),
                "Http Status code should be BAD");
        Assertions.assertThrows(GlobalRuntimeException.class, ()->{
            accountService.register(requestDto);
        });

        // Arrange
        requestDto.setUserName("emre123");
        requestDto.setEmail("emretantu_re@gmail.com");
        request = new HttpEntity<>(requestDto, headers);

        // Act
        response = testRestTemplate.postForEntity("/account/register",request, Person.class);

        // Assert
        Assertions.assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode(),
                "Http Status code should be BAD");
        Assertions.assertThrows(GlobalRuntimeException.class, ()->{
            accountService.register(requestDto);
        });
    }

    @Test
    @DisplayName("User cannot see self info without JWT")
    @Order(3)
    void infoTest_withoutToken_willBeFiltered() throws JsonProcessingException {

        // Arrange

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.exchange(
                "/account/info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Person>() {
                });
        Person person = response.getBody();

        // Assert
        Assertions.assertEquals(
                HttpStatus.FORBIDDEN,
                response.getStatusCode(),
                "Http Status code should be 403");
    }

    @Test
    @DisplayName("Can login with username or email")
    @Order(4)
    void loginTest_whenInputsOk_returnsJWT() throws JsonProcessingException, InterruptedException {

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("emre123");
        requestDto.setPassword("123qweqwe");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<JwtToken> response = testRestTemplate.postForEntity("/account/login",request, JwtToken.class);
        JwtToken jwtToken = response.getBody();
        jwtLeatest =jwtToken.getToken();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be OK");
        Assertions.assertNotNull(
                jwtToken,
                "JwtToken should not be null");

        // Arrange
        requestDto.setUsernameOrEmail("emretantu@gmail.com");
        request = new HttpEntity<>(requestDto, headers);

        // Act
        Thread.sleep(1000);
        response = testRestTemplate.postForEntity("/account/login",request, JwtToken.class);
        jwtToken = response.getBody();
        jwtOld = jwtLeatest;
        jwtLeatest = jwtToken.getToken();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be OK");
        Assertions.assertNotNull(
                jwtToken,
                "JwtToken should not be null");
    }

    @Test
    @DisplayName("Can can see self info")
    @Order(5)
    void infoTest_withToken_returnsPerson() throws JsonProcessingException {

        // Arrange

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.exchange(
                "/account/info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Person>() {
                });
        Person person = response.getBody();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be OK");
        Assertions.assertEquals(person.getUsername(),"emre123");
    }

    @Test
    @DisplayName("Old tokens don't work")
    @Order(6)
    void infoTest_withOldToken_returns403() throws JsonProcessingException {

        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtOld);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.exchange(
                "/account/info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Person>() {
                });

        // Assert
        Assertions.assertNotEquals(jwtOld,jwtLeatest);
        Assertions.assertEquals(
                HttpStatus.FORBIDDEN,response.getStatusCode(),
                "Http Status code should be OK");
    }

    @Test
    @DisplayName("User can't change password when password not matched")
    @Order(7)
    void changePasswordTest_whenInputsNotOk_throwsGlobalRuntimeException(){

        // Arrange
        PersonChangePasswordDTO inputDto = new PersonChangePasswordDTO();
        inputDto.setOldPassword("sdfgsdf");
        inputDto.setNewPassword("123123213");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonChangePasswordDTO> request = new HttpEntity<>(inputDto, headers);

        // Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/account/changepassword",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {});
        String result = response.getBody();

        // Asset
        Assertions.assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode(),
                "Http status code should be 400");
        Assertions.assertTrue(response.getBody().contains("Password is incorrect! "));
    }

    @Test
    @DisplayName("User can change password")
    @Order(8)
    void changePasswordTest_whenInputsOk_returnsInfoString(){

        // Arrange
        PersonChangePasswordDTO inputDto = new PersonChangePasswordDTO();
        inputDto.setOldPassword("123qweqwe");
        inputDto.setNewPassword("123123123");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonChangePasswordDTO> request = new HttpEntity<>(inputDto, headers);

        // Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/account/changepassword",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<String>() {});
        String result = response.getBody();

        // Asset
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http status code should be 200");
        Assertions.assertNotNull(
                result,
                "Info should not be null");
    }

    @Test
    @DisplayName("Auto logout after change password")
    @Order(9)
    void infoTest_afterLogout_throwsGlobalRuntimeException() throws JsonProcessingException {

        // Arrange

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.exchange(
                "/account/info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Person>() {
                });
        Person person = response.getBody();

        // Assert
        Assertions.assertEquals(
                HttpStatus.FORBIDDEN,
                response.getStatusCode(),
                "Http Status code should be 403");
    }

    @Test
    @DisplayName("Can login with new password")
    @Order(10)
    void loginTest_withNewPassword_returnsJWT() throws JsonProcessingException, InterruptedException {

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("emre123");
        requestDto.setPassword("123123123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<JwtToken> response = testRestTemplate.postForEntity("/account/login",request, JwtToken.class);
        JwtToken jwtToken = response.getBody();
        jwtLeatest =jwtToken.getToken();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be OK");
        Assertions.assertNotNull(
                jwtToken,
                "JwtToken should not be null");
    }

    @Test
    @DisplayName("User can update self infos")
    @Order(11)
    void personUpdateTest_whenInputsOk_returnsPerson(){

        // Arrange
        PersonUpdateDTO inputDto = new PersonUpdateDTO();
//        inputDto.setEmail("");
        inputDto.setSecretKey("emre123");
        inputDto.setFirstName("Emre");
        inputDto.setLastName("");
        inputDto.setPhoneNumber("538 042 9999");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonUpdateDTO> request = new HttpEntity<>(inputDto, headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.exchange(
                "/account",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<Person>() {});
        Person person = response.getBody();

        // Asset
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http status code should be 200");
        Assertions.assertNotNull(
                person.getSecretKey(),
                "Info should not be null");
    }

    @Test
    @DisplayName("Second person created")
    @Order(12)
    void registerTest_forSecondUser_returnsPerson() throws JsonProcessingException {

        // Arrange
        PersonDTO requestDto = new PersonDTO();
        requestDto.setUserName("gokhan123");
        requestDto.setPassword("123qweqwe");
        requestDto.setFirstName("Gökhan");
        requestDto.setLastName("Güneşoğlu");
        requestDto.setEmail("gokhangunesoglu@gmail.com");
        requestDto.setPhoneNumber("538 042 0000");
        requestDto.setAuthority(Authority.USER);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.postForEntity("/account/register",request, Person.class);

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be OK");
    }

    @Test
    @DisplayName("User can't change email already use")
    @Order(13)
    void personUpdateTest_whenEmailUsed_throwsGlobalRuntimeException(){

        // Arrange
        PersonUpdateDTO inputDto = new PersonUpdateDTO();
        inputDto.setEmail("gokhangunesoglu@gmail.com");
        inputDto.setSecretKey("emre123");
        inputDto.setFirstName("Emre");
        inputDto.setLastName("");
        inputDto.setPhoneNumber("538 042 9999");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonUpdateDTO> request = new HttpEntity<>(inputDto, headers);

        // Act
        ResponseEntity<GlobalRuntimeException> response = testRestTemplate.exchange(
                "/account",
                HttpMethod.PUT,
                request,
                new ParameterizedTypeReference<GlobalRuntimeException>() {});

        // Asset
        Assertions.assertEquals(
                HttpStatus.BAD_REQUEST,
                response.getStatusCode(),
                "Http status code should be 400");
        Assertions.assertEquals(response.getBody().getMessage(),
                "Email is already uses! gokhangunesoglu@gmail.com",
                "email:gokhangunesoglu@gmail.com shouldnot be accept");
    }

    @Test
    @DisplayName("User can logout")
    @Order(14)
    void logoutTest_withoutToken_willBeFiltered() throws JsonProcessingException {

        // Arrange

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/account/logout",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<String>() {
                });
        String message = response.getBody();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be 200");

        Assertions.assertNotNull(
                message,
                "Logout message should not be null"
        );
    }
    @Test
    @DisplayName("User cannot see self info after logout")
    @Order(15)
    void infoTest_afterLogout_willBeFiltered() throws JsonProcessingException {

        // Arrange

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(headers);

        // Act
        ResponseEntity<Person> response = testRestTemplate.exchange(
                "/account/info",
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<Person>() {
                });
        Person person = response.getBody();

        // Assert
        Assertions.assertEquals(
                HttpStatus.FORBIDDEN,
                response.getStatusCode(),
                "Http Status code should be 403");

        Assertions.assertNull(
                person,
                "Person should be null");
    }

    @Test
    @DisplayName("Can't login 3 times in 1 hour")
    @Order(16)
    void loginTest_afterLogout_throwsGlobalRuntimeException() throws JsonProcessingException, InterruptedException {

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("emre123");
        requestDto.setPassword("123123123");


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<JwtToken> response = testRestTemplate.postForEntity("/account/login",request, JwtToken.class);
        response = testRestTemplate.postForEntity("/account/login",request, JwtToken.class);
        JwtToken jwtToken = response.getBody();
        jwtLeatest =jwtToken.getToken();

        // Assert
        Assertions.assertEquals(
                HttpStatus.TOO_MANY_REQUESTS,
                response.getStatusCode(),
                "Http Status code should be 429");
    }

    @Test
    @DisplayName("1 Day block if continue")
    @Order(17)
    void loginTest_afterMultiLoginTry_throwsGlobalRuntimeException() throws JsonProcessingException, InterruptedException {

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("emre123");
        requestDto.setPassword("123123123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<JwtToken> response = testRestTemplate.postForEntity("/account/login",request, JwtToken.class);
        JwtToken jwtToken = response.getBody();
        jwtLeatest =jwtToken.getToken();

        // Assert
        Assertions.assertEquals(
                HttpStatus.LOCKED,
                response.getStatusCode(),
                "Http Status code should be 423");
    }

    @Test
    @DisplayName("Second user(gokhan123) login")
    @Order(18)
    void loginTest_withSecondUser_returnsJWT() throws JsonProcessingException, InterruptedException {

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("gokhan123");
        requestDto.setPassword("123qweqwe");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<JwtToken> response = testRestTemplate.postForEntity("/account/login",request, JwtToken.class);
        JwtToken jwtToken = response.getBody();
        jwtLeatest =jwtToken.getToken();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be 200");
        Assertions.assertNotNull(
                jwtToken,
                "JwtToken should not be null");
    }

    @Test
    @DisplayName("User can't remove self when inputs incorrect")
    @Order(19)
    void disableTest_withSecondUser_throwsGlobalRuntimeException() throws JsonProcessingException, InterruptedException {

        // -- WHEN USERNAME NOT MATCHED --

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("gokhan12");
        requestDto.setPassword("123qweqwe");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<GlobalRuntimeException> response = testRestTemplate.exchange(
                "/account",
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<GlobalRuntimeException>() {});


        // Assert
        Assertions.assertEquals(
                HttpStatus.NOT_ACCEPTABLE,
                response.getStatusCode(),
                "Http Status code should be 406");

        // -- WHEN EMAIL NOT MATCHED --

        // Arrange
        requestDto.setUsernameOrEmail("gokhangunesoglu@gmail.co");
        request = new HttpEntity<>(requestDto, headers);

        // Act
        response = testRestTemplate.exchange(
                "/account",
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<GlobalRuntimeException>() {});

        // Assert
        Assertions.assertEquals(
                HttpStatus.NOT_ACCEPTABLE,
                response.getStatusCode(),
                "Http Status code should be 406");


        // -- WHEN PASSWORD NOT MATCHED --

        // Arrange
        requestDto.setPassword("123qweqw");
        requestDto.setUsernameOrEmail("gokhan123");
        request = new HttpEntity<>(requestDto, headers);

        // Act
        response = testRestTemplate.exchange(
                "/account",
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<GlobalRuntimeException>() {});

        // Assert
        Assertions.assertEquals(
                HttpStatus.NOT_ACCEPTABLE,
                response.getStatusCode(),
                "Http Status code should be 406");
    }

    @Test
    @DisplayName("User can remove self with username")
    @Order(20)
    void disableTest_withSecondUser_returnsString() throws JsonProcessingException, InterruptedException {

        // -- WHEN USERNAME NOT MATCHED --

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("gokhan123");
        requestDto.setPassword("123qweqwe");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtLeatest);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<String> response = testRestTemplate.exchange(
                "/account",
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<String>() {});
        String responseString = response.getBody();

        // Assert
        Assertions.assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "Http Status code should be 200");
        Assertions.assertEquals(
                responseString,
                "Account removed!",
                "Response message should me \"Account removed!\""
        );
    }

    @Test
    @DisplayName("Second user can't login cause of remove")
    @Order(21)
    void loginTest_withSecondUser_afterRemove_throwsGlobalRuntimeException() throws JsonProcessingException, InterruptedException {

        // Arrange
        PersonLoginDTO requestDto = new PersonLoginDTO();
        requestDto.setUsernameOrEmail("gokhan123");
        requestDto.setPassword("123qweqwe");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity<PersonLoginDTO> request = new HttpEntity<>(requestDto, headers);

        // Act
        ResponseEntity<GlobalRuntimeException> response = testRestTemplate.postForEntity("/account/login",request, GlobalRuntimeException.class);
        GlobalRuntimeException exception = response.getBody();


        // Assert
        Assertions.assertEquals(
                HttpStatus.NOT_FOUND,
                response.getStatusCode(),
                "Http Status code should be 400");
        Assertions.assertNotNull(
                exception,
                "exception should not be null");
        Assertions.assertEquals(
                exception.getMessage(),
                "This user is removed.",
                "Message should be \"This user is removed.\""
        );
    }
}
