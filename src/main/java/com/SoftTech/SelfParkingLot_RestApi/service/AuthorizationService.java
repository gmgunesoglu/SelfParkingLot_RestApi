package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.Authorization;

import java.util.List;

public interface AuthorizationService {

    List<Authorization> getAll();
    Authorization get(Long id);
    Authorization save(Authorization authorization);
    Authorization delete(Long id);
    Authorization update(Authorization authorization,Long id);
}
