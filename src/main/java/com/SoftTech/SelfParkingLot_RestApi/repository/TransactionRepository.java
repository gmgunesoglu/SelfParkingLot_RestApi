package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.PaymentRecipe;
import com.SoftTech.SelfParkingLot_RestApi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {



    Transaction getTransactionByPlateAndExitDateAndEnable(String plate, Date exitDate, boolean enable);

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDTO(pr.hours2,pr.hours4,pr.hours6,pr.hours10,pr.hours24) FROM Transaction t " +
            "JOIN ParkingSpot ps ON t.parkingSpotId=ps.id " +
            "JOIN PaymentRecipe pr ON ps.paymentRecipeId=pr.id WHERE t.plate=:plate")
    PaymentRecipeDTO getPaymentRecipeOfPlate(String plate);

//    @Query("SELECT PaymentRecipe FROM PaymentRecipe pr WHERE pr.id = " +
//            "(SELECT ps.paymentRecipeId FROM ParkingSpot ps WHERE ps.id = " +
//            "(SELECT t.parkingSpotId FROM Transaction t WHERE t.plate = :plate AND t.enable = true AND t.exitDate = null))")
//    PaymentRecipe getPaymentRecipeOfPlate(String plate);


}
