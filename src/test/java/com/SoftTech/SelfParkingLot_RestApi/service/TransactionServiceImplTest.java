package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.CheckInDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.CheckInInfoDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingSpot;
import com.SoftTech.SelfParkingLot_RestApi.entity.PaymentRecipe;
import com.SoftTech.SelfParkingLot_RestApi.entity.Transaction;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingSpotRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.PaymentRecipeRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private ParkingSpotRepository parkingSpotRepository;
    @Mock
    private PaymentRecipeRepository paymentRecipeRepository;

    @Test
    public void checkIn(){
        CheckInDTO input = new CheckInDTO();
        input.setParkingSpotId(10001L);
        input.setVehiclePlate("34ZEK0001");
        input.setParkingSpotId(10010L);
        ParkingSpot parkingSpot = Mockito.mock(ParkingSpot.class);

        Mockito.when(parkingSpotRepository.getParkingSpotByIdAndEnable(input.getParkingSpotId(),true)).thenReturn(parkingSpot);
        Mockito.when(transactionRepository.getTransactionByPlateAndExitDateAndEnable(input.getVehiclePlate(),null,true)).thenReturn(Mockito.mock(Transaction.class));
        Mockito.when(paymentRecipeRepository.getByIdAndEnable(parkingSpot.getPaymentRecipeId(),true)).thenReturn(Mockito.mock(PaymentRecipe.class));


        CheckInInfoDTO output = transactionService.checkIn(input);
        System.out.println(output.getName());
    }
}
