package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.ParkingLotWithTListDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDTO;
import com.SoftTech.SelfParkingLot_RestApi.dto.PaymentRecipeDetailDTO;
import com.SoftTech.SelfParkingLot_RestApi.entity.PaymentRecipe;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingLotRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.PaymentRecipeRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.SharedParkingLotRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentRecipeServiceImpl implements PaymentRecipeService{

    private final PaymentRecipeRepository paymentRecipeRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final SharedParkingLotRepository sharedParkingLotRepository;
    private final JwtService jwtService;


    @Override
    public List<ParkingLotWithTListDTO<PaymentRecipeDetailDTO>> getAll(HttpServletRequest request) {
        // kullanıcının id sini çek
        Long ownerId = getOwnerIdFromRequest(request);
        // kullanıcının kendi otoparkların id lerini çek
        List<Long> parkingLotIds = parkingLotRepository.getIds(ownerId,true);
        // kullanıcının ortağı olduğu otoparkların id lerini ekle
        parkingLotIds.addAll(sharedParkingLotRepository.getParkingLotIds(ownerId));
        // bu id lere ait ödeme tarifelerini döndür
        List<ParkingLotWithTListDTO<PaymentRecipeDetailDTO>> dtos = new ArrayList<>();
        ParkingLotWithTListDTO<PaymentRecipeDetailDTO> dto;
        for (Long parkingLotId : parkingLotIds){
            dto=parkingLotRepository.getParkingLotWithTListDTO(parkingLotId);
            //bu otoparkın ödeme tarifelerini çek, dto ya ekle
            dto.setTList(paymentRecipeRepository.getPaymentRecipeDetailDTOS(parkingLotId));
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public PaymentRecipeDetailDTO get(HttpServletRequest request, Long id) {
        // girilen id ok?
        PaymentRecipe paymentRecipe = paymentRecipeRepository.getByIdAndEnable(id,true);
        if(paymentRecipe==null){
            throw new GlobalRuntimeException("Payment recipe not found! ", HttpStatus.NOT_FOUND);
        }
        // owner veya partner mı ?
        Long ownerId = getOwnerIdFromRequest(request);
        if(!ownerId.equals(parkingLotRepository.getOwnerIdById(paymentRecipe.getParkingLotId()))){
            // owner değil ise
            if(sharedParkingLotRepository.getSharedParkingLotByParkingLotIdAndPartnerId(paymentRecipe.getParkingLotId(),ownerId)==null){
                //partner da değil ise hata gönder
                throw new GlobalRuntimeException("You cant see other users payment recipes! ", HttpStatus.UNAUTHORIZED);
            }
        }
        //veriyi gönder...
        return paymentRecipeRepository.getPaymentRecipeDetailDTO(id);
    }

    @Override
    public PaymentRecipeDTO add(HttpServletRequest request, PaymentRecipeDTO dto) {
        // parkingLotId yi kontrol et varmı?
        if(parkingLotRepository.checkWithIdAndEnable(dto.getParkingLotId())==null){
            throw new GlobalRuntimeException("Parking lot not found! ",HttpStatus.NOT_FOUND);
        }
        // varsa owner veya parner mı ?
        Long ownerId=getOwnerIdFromRequest(request);
        if(parkingLotRepository.checkWithIdAndOwnerIdAndEnable(dto.getParkingLotId(),ownerId)==null){
            // owner değil
            if(sharedParkingLotRepository.checkWithPartnerIdAndParkingLotId(ownerId,dto.getParkingLotId())==null){
                // partner da değil
                throw new GlobalRuntimeException("You can't add parking spot to other users parking lot ",HttpStatus.UNAUTHORIZED);
            }
        }
        // UK-> parkingLotId ve commet, daha önce silinmiş ise onu enable et ama güncel verilerle.
        PaymentRecipe paymentRecipe = paymentRecipeRepository.getPaymentRecipeByParkingLotIdAndTagAndEnable(dto.getParkingLotId(),dto.getTag(),false);
        if(paymentRecipe!=null){
            // daha önce bu satır varmış disable edilmiş
        }else{
            paymentRecipe = new PaymentRecipe();
        }
        paymentRecipe.setEnable(true);
        paymentRecipe.setTag(dto.getTag());
        paymentRecipe.setHours2(dto.getHours2());
        paymentRecipe.setHours4(dto.getHours4());
        paymentRecipe.setHours6(dto.getHours6());
        paymentRecipe.setHours10(dto.getHours10());
        paymentRecipe.setHours24(dto.getHours24());
        paymentRecipe.setParkingLotId(dto.getParkingLotId());
        paymentRecipeRepository.save(paymentRecipe);
        return dto;
    }

    @Override
    public PaymentRecipeDTO update(HttpServletRequest request, PaymentRecipeDTO dto, Long id) {
        // satırı kontrol et
        PaymentRecipe paymentRecipe = paymentRecipeRepository.getByIdAndEnable(id,true);
        if(paymentRecipe == null){
            throw new GlobalRuntimeException("Payment recipe not found! ",HttpStatus.NOT_FOUND);
        }
        // kullanıcıyı kontrol et
        Long ownerId = getOwnerIdFromRequest(request);
        if(parkingLotRepository.checkWithIdAndOwnerIdAndEnable(dto.getParkingLotId(),ownerId)==null){
            // owner değil
            if(sharedParkingLotRepository.checkWithPartnerIdAndParkingLotId(ownerId,dto.getParkingLotId())==null){
                // partner da değil
                throw new GlobalRuntimeException("You can't update parking spot to other users parking lot ",HttpStatus.UNAUTHORIZED);
            }
        }
        // update et
        paymentRecipe.setTag(dto.getTag());
        paymentRecipe.setHours2(dto.getHours2());
        paymentRecipe.setHours4(dto.getHours4());
        paymentRecipe.setHours6(dto.getHours6());
        paymentRecipe.setHours10(dto.getHours10());
        paymentRecipe.setHours24(dto.getHours24());
        paymentRecipe.setParkingLotId(dto.getParkingLotId());
        paymentRecipeRepository.save(paymentRecipe);
        return dto;
    }

    @Override
    public String disable(HttpServletRequest request, Long id) {
        // satırı kontrol et
        PaymentRecipe paymentRecipe = paymentRecipeRepository.getByIdAndEnable(id,true);
        if(paymentRecipe == null){
            throw new GlobalRuntimeException("Payment recipe not found! ",HttpStatus.NOT_FOUND);
        }
        // kullanıcıyı kontrol et
        Long ownerId = getOwnerIdFromRequest(request);
        if(parkingLotRepository.checkWithIdAndOwnerIdAndEnable(paymentRecipe.getParkingLotId(),ownerId)==null){
            // owner değil
            if(sharedParkingLotRepository.checkWithPartnerIdAndParkingLotId(ownerId,paymentRecipe.getParkingLotId())==null){
                // partner da değil
                throw new GlobalRuntimeException("You can't remove parking spot from other users parking lot ",HttpStatus.UNAUTHORIZED);
            }
        }
        // disable et
        paymentRecipe.setEnable(false);
        paymentRecipeRepository.save(paymentRecipe);
        return "Payment recipe removed!";
    }

    private Long getOwnerIdFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        return jwtService.extractId(token);
    }


}
