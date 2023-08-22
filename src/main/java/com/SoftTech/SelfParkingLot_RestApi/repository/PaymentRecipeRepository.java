package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDetailDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.PaymentRecipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRecipeRepository extends JpaRepository<PaymentRecipe,Long> {


    List<PaymentRecipe> findAllByParkingLotId(Long parkingLotId);

    List<PaymentRecipe> getAllByParkingLotIdAndEnable(Long parkingLotId, boolean enable);

    PaymentRecipe getByIdAndEnable(Long id, boolean enable);

    @Query("SELECT true FROM PaymentRecipe p WHERE p.parkingLotId = :parkingLotId AND p.tag = :tag")
    Boolean checkWithParkingLotIdAndTag(Long parkingLotId, String tag);

    PaymentRecipe getPaymentRecipeByParkingLotIdAndTagAndEnable(Long parkingLotId, String tag, boolean enable);

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDetailDTO(p.id,p.tag,p.hours2,p.hours4,p.hours6,p.hours10,p.hours24) FROM " +
            "PaymentRecipe p WHERE p.parkingLotId = :parkingLotId AND p.enable = true")
    List<PaymentRecipeDetailDTO> getPaymentRecipeDetailDTOS(Long parkingLotId);

    @Query("SELECT new com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDetailDTO(p.id,p.tag,p.hours2,p.hours4,p.hours6,p.hours10,p.hours24) FROM " +
            "PaymentRecipe p WHERE p.parkingLotId = :parkingLotId AND p.enable = true")
    PaymentRecipeDetailDTO getPaymentRecipeDetailDTO(Long parkingLotId);

    @Query("SELECT p.parkingLotId FROM PaymentRecipe p WHERE p.id=:id")
    Long getParkingLotId(Long id);
}
