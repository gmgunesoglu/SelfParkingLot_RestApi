package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();
    User get(Long id);
    User save(User user);
    User delete(Long id);
    User update(User user,Long id);
}
