package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.LocationDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import com.SoftTech.SelfParkingLot_RestApi.repository.LocationRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingLotRepository;
import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class LocationServiceImplTest {

    @InjectMocks
    private LocationServiceImpl locationService;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Test
    @Description("Can add location")
    public void saveLocation_whenInputsNormal(){
        LocationDTO input = new LocationDTO();
        input.setDistrict("district-test");
        input.setTown("town-test");
        input.setCity("city-test");

        Mockito.when(locationRepository.save(ArgumentMatchers.any(Location.class))).thenReturn(new Location());
        Location output = locationService.add(input);

        Assertions.assertEquals(output.getCity(),input.getCity().toUpperCase());
        Assertions.assertEquals(output.getTown(),input.getTown().toUpperCase());
        Assertions.assertEquals(output.getDistrict(),input.getDistrict().toUpperCase());

        input.setDistrict("istanbul");
        input.setTown("suadiye");
        input.setCity("kadıköy");
        output = locationService.add(input);

        Assertions.assertEquals(output.getCity(),input.getCity().toUpperCase());
        Assertions.assertEquals(output.getTown(),input.getTown().toUpperCase());
        Assertions.assertEquals(output.getDistrict(),input.getDistrict().toUpperCase());
    }




}
