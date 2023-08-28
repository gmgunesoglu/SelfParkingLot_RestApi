package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.CheckInDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.CheckInInfoDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.VehicleType;
import com.SoftTech.SelfParkingLot_RestApi.service.LocationService;
import com.SoftTech.SelfParkingLot_RestApi.service.ParkingSpotService;
import com.SoftTech.SelfParkingLot_RestApi.service.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(controllers = ParkController.class)
public class ParkControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;
    @MockBean
    private ParkingSpotService parkingSpotService;
    @MockBean
    private TransactionService transactionService;


    @Test
    @DisplayName("Vehicle should not park when it is already in park")
    void checkInTest_whenPlateIsAlreadyInPark() throws Exception {

        // Arrange
        CheckInDTO checkInDTO = new CheckInDTO();
        checkInDTO.setParkingSpotId(10010L);
        checkInDTO.setVehiclePlate("34ZEK0001");
        checkInDTO.setVehicleType(VehicleType.CAR);
        Long parkingLotId = 10001L;

        RequestBuilder request = MockMvcRequestBuilders.post("/customer/park/parkinglots/"+parkingLotId+"/parkingspots")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(checkInDTO));

        Mockito.when(transactionService.checkIn(Mockito.any(CheckInDTO.class))).thenReturn(new CheckInInfoDTO());

        // Act
        MvcResult mvcResult = mockMvc.perform(request).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        CheckInInfoDTO result = new ObjectMapper().readValue(responseBodyAsString,CheckInInfoDTO.class);

        // Assert
        Assertions.assertEquals(result.getVehiclePlate(),checkInDTO.getVehiclePlate());
        Assertions.assertEquals(result.getParkingLotId(),parkingLotId);

    }
}
