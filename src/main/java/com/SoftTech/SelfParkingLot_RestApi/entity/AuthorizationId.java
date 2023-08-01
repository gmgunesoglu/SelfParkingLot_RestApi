package com.SoftTech.SelfParkingLot_RestApi.entity;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class AuthorizationId implements Serializable {

    private Long userId;

    private  String authority;
}
