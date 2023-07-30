package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.Authorization;
import com.SoftTech.SelfParkingLot_RestApi.entity.AuthorizationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationRepository extends JpaRepository<Authorization, AuthorizationId> {
}
