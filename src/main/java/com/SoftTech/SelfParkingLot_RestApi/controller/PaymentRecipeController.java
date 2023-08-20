package com.SoftTech.SelfParkingLot_RestApi.controller;

import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingLotWithTListDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDetailDTO;
import com.SoftTech.SelfParkingLot_RestApi.service.PaymentRecipeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/paymentrecipes")
@RequiredArgsConstructor
public class PaymentRecipeController {

    private final PaymentRecipeService paymentRecipeService;

    @GetMapping
    public List<ParkingLotWithTListDTO<PaymentRecipeDetailDTO>> getAll(HttpServletRequest request){
        return paymentRecipeService.getAll(request);
    }

    @GetMapping("/{id}")
    public PaymentRecipeDetailDTO get(HttpServletRequest request, @PathVariable Long id){
        return paymentRecipeService.get(request,id);
    }

    @PostMapping
    public PaymentRecipeDTO add(HttpServletRequest request,@RequestBody PaymentRecipeDTO dto){
        return paymentRecipeService.add(request,dto);
    }

    @PutMapping("{id}")
    public PaymentRecipeDTO update(HttpServletRequest request, @RequestBody PaymentRecipeDTO dto, @PathVariable Long id){
        return paymentRecipeService.update(request,dto,id);
    }

    @DeleteMapping("/{id}")
    public String disable(HttpServletRequest request, @PathVariable Long id){
        return paymentRecipeService.disable(request,id);
    }
}
