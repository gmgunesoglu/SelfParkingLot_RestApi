package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.Authorization;
import com.SoftTech.SelfParkingLot_RestApi.repository.AuthorizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthhorizationServiceImpl implements AuthorizationService{

    private final AuthorizationRepository authorizationRepository;

    @Override
    public List<Authorization> getAll() {
        return authorizationRepository.findAll();
    }

    @Override
    public Authorization get(Long userId) {
        //sonra
        return null;
    }

    @Override
    public Authorization save(Authorization authorization) {
        return authorizationRepository.save(authorization);
    }

    @Override
    public Authorization delete(Long id) {
        //sonra
        return null;
    }

    @Override
    public Authorization update(Authorization authorization, Long id) {
        //sonra
        return null;
    }
}
