package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.PersonDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Authority;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.service.AccountService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = AccountController.class,excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class AccountControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @DisplayName("User can be create")
    void registerTest_whenInputsNormal() throws Exception {

        // Arrange
        PersonDTO inputDto = new PersonDTO();
        inputDto.setAuthority(Authority.USER);
        inputDto.setEmail("gokhangunesoglu@gmail.com");
        inputDto.setPassword("123qweqwe");
        inputDto.setFirstName("Gökhan");
        inputDto.setLastName("Güneşoğlu");
        inputDto.setUserName("gokhan123");
        inputDto.setPhoneNumber("538 042 6061");

        RequestBuilder request = MockMvcRequestBuilders.post("/account/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(inputDto));

        Mockito.when(accountService.register(Mockito.any(PersonDTO.class))).thenReturn(new Person());

        // Act
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        Person person = new ObjectMapper().readValue(responseBodyAsString,Person.class);

        // Assert
        Assertions.assertEquals(inputDto.getEmail(),person.getEmail());

    }

}
