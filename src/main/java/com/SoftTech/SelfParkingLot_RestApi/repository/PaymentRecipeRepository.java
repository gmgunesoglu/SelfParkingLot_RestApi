package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.PaymentRecipe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRecipeRepository extends JpaRepository<PaymentRecipe,Long> {
}
