package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingSpot;
import com.SoftTech.SelfParkingLot_RestApi.entity.PaymentRecipe;
import com.SoftTech.SelfParkingLot_RestApi.entity.Transaction;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingSpotRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.PaymentRecipeRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.TransactionRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    private final ParkingSpotRepository parkingSpotRepository;
    private final PaymentRecipeRepository paymentRecipeRepository;

    @Override
    public CheckInInfoDTO checkIn(CheckInDTO dto) {

        // parking spot u kontrol et
        // 1. öyle bir spot varmı
        ParkingSpot parkingSpot = parkingSpotRepository.getParkingSpotByIdAndEnable(dto.getParkingSpotId(),true);
        if(parkingSpot==null){
            throw new GlobalRuntimeException("Pakring spot not found! ", HttpStatus.NOT_FOUND);
        }

        // 2 spot boşmu
        if(parkingSpot.isOccupied()){
            throw new GlobalRuntimeException("Parking spot occupied! ",HttpStatus.BAD_REQUEST);
        }

        // araç başka yerde park halinde mi?
        Transaction transaction = transactionRepository.getTransactionByPlateAndExitDateAndEnable(dto.getVehiclePlate(),null,true);
        if(transaction!=null){
            throw new GlobalRuntimeException("This vehicle is already in park! ",HttpStatus.BAD_REQUEST);
        }

        // 3 araç tipi uyuyormu
        int code = (int)Math.pow(2,dto.getVehicleType().ordinal());
        if((parkingSpot.getVehicleTypeCode()&code)!=code){
            throw new GlobalRuntimeException("You can't park to this spot! ",HttpStatus.BAD_REQUEST);
        }

        // park et...
        transaction = new Transaction();
        transaction.setPlate(dto.getVehiclePlate());
        transaction.setParkingSpotId(dto.getParkingSpotId());
        transaction.setEnteryDate(new Date(System.currentTimeMillis()));
        transaction.setEnable(true);
        transactionRepository.save(transaction);
        parkingSpot.setOccupied(true);
        parkingSpotRepository.save(parkingSpot);

        // info gönder
        CheckInInfoDTO info = new CheckInInfoDTO();
        info.setParkingLotId(parkingSpot.getParkingLotId());
        info.setName(parkingSpot.getName());
        info.setIndoor(parkingSpot.isIndoor());
        info.setVehiclePlate(dto.getVehiclePlate());
        info.setEnteryDate(transaction.getEnteryDate());
        PaymentRecipe paymentRecipe=paymentRecipeRepository.getByIdAndEnable(parkingSpot.getPaymentRecipeId(),true);
        info.setHours2Price(paymentRecipe.getHours2());
        info.setHours4Price(paymentRecipe.getHours4());
        info.setHours6Price(paymentRecipe.getHours6());
        info.setHours10Price(paymentRecipe.getHours10());
        info.setHours24Price(paymentRecipe.getHours24());
        return info;
    }

    @Override
    public PaymentTokenDTO getPaymentToken(CardDTO dto) {
        Stripe.apiKey="pk_test_51NgjmOHuSNzjvuLsqnqGaRRi0TE4uXgfj0ICIZqWVqHOnsaCGRs5fzWm37zlsJhQCZYJRk8KFMAgOhM6bdZVqjlR00BNi0CqDc";
        Map<String, Object> cardParams = new HashMap<>();
        cardParams.put("number", dto.getCardNumber());
        cardParams.put("exp_month", dto.getExpMonth());
        cardParams.put("exp_year", dto.getExpYear());
        cardParams.put("cvc", dto.getCvc());
        Map<String, Object> tokenParams = new HashMap<>();
        tokenParams.put("card", cardParams);
        try{
            Token token = Token.create(tokenParams);
            PaymentTokenDTO paymentTokenDTO=new PaymentTokenDTO();
            paymentTokenDTO.setStripeToken(token.getId());
            return paymentTokenDTO;
        }catch (StripeException e){
            log.error(e.getMessage());
            throw new GlobalRuntimeException(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public PaymentInfoDTO showPaymentInfo(String vehiclePlate) {

        // plakadan transaction kontrol et
        Transaction transaction = transactionRepository.getTransactionByPlateAndExitDateAndEnable(vehiclePlate,null,true);
        if(transaction==null){
            throw new GlobalRuntimeException("Transaction not found for this vehicle: "+vehiclePlate,HttpStatus.NOT_FOUND);
        }

        // aracın giriş tarihinden ücreti hesapla
        PaymentRecipeDTO recipe = transactionRepository.getPaymentRecipeOfPlate(vehiclePlate);
        Date exitDate = new Date(System.currentTimeMillis());
        int amount = calculateAmount(transaction.getEnteryDate(),exitDate,recipe);

        // PaymentInfoDTO gönder
        PaymentInfoDTO info = new PaymentInfoDTO();
        info.setAmount(amount);
        info.setCurrency("TRY");
        return info;
    }

    @Override
    public CheckOutInfoDTO checkOut(CheckOutDTO dto) {

        // plakanın son transaction ını çek ve kontrol et
        Transaction transaction = transactionRepository.getTransactionByPlateAndExitDateAndEnable(dto.getVehiclePlate(),null,true);
        if(transaction==null){
            throw new GlobalRuntimeException("Transaction not found for this vehicle: "+dto.getVehiclePlate(),HttpStatus.NOT_FOUND);
        }

        // amount ı kontrol et
        Date exitDate = new Date(System.currentTimeMillis());
        PaymentRecipeDTO recipe = transactionRepository.getPaymentRecipeOfPlate(dto.getVehiclePlate());
        int amount = calculateAmount(transaction.getEnteryDate(),exitDate,recipe);
        if(amount!=dto.getAmount()){
            throw new GlobalRuntimeException("Amount not matched! ",HttpStatus.BAD_REQUEST);
        }

        // charge için parametreleri oluştur
        DescriptionDTO descriptionDTO = parkingSpotRepository.getDescriptionDTO(transaction.getParkingSpotId());
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int)(dto.getAmount() * 100));
        chargeParams.put("currency", "TRY");
        chargeParams.put("description", descriptionDTO.toString());
        chargeParams.put("source", dto.getPaymentToken());

        // ödemeyi yap
        try {
            Stripe.apiKey="sk_test_51NgjmOHuSNzjvuLseCeRO5y5bXmRIuR0jz5VTHw1SmKu3ZJB0BDyhKa4F8RXZo1qvuGZyGyv7IMioL9rR8bZ9g0R00KixB9LE3";
            Charge charge = Charge.create(chargeParams);
            if(charge.getId()==null || !charge.getPaid()){
                throw new GlobalRuntimeException("Some error about payment service! ",HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (StripeException e) {
            log.error("StripeService (charge)", e);
            throw new RuntimeException(e.getMessage());
        }

        // transaction ve parkingspot satırını güncelle kaydet
        transaction.setExitDate(exitDate);
        transaction.setPaymentToken(dto.getPaymentToken());
        transactionRepository.save(transaction);
        ParkingSpot parkingSpot = parkingSpotRepository.getParkingSpotByIdAndEnable(transaction.getParkingSpotId(),true);
        parkingSpot.setOccupied(false);
        parkingSpotRepository.save(parkingSpot);

        //infoDTO yu gönder
        return CheckOutInfoDTO.builder()
                .parkingLotName(descriptionDTO.getParkingLotName())
                .enteryDate(transaction.getEnteryDate())
                .exitDate(transaction.getExitDate())
                .amount(amount+" TRY")
                .vehiclePlate(transaction.getPlate())
                .build();
    }

    private int calculateAmount(Date enteryDate, Date exitDate, PaymentRecipeDTO recipe){
        long duration = enteryDate.getTime() - exitDate.getTime();
        long seconds = duration/1000;
        if(duration%1000!=0){
            seconds++;
        }
        long minutes = seconds/60;
        if(seconds%60!=0){
            minutes++;
        }
        int hours=(int)minutes/60;
        if(minutes%60!=0){
            hours++;
        }
        int amount=recipe.getHours24()*(hours/24);
        hours%=24;
        if(hours>10){
            amount+=recipe.getHours24();
        }else if(hours>6){
            amount+=recipe.getHours10();
        }else if(hours>4){
            amount+=recipe.getHours6();
        }else if(hours>2){
            amount+=recipe.getHours4();
        }else if(hours>0){
            amount+=recipe.getHours2();
        }
        return amount;
    }


}
