package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingSpot;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingLotRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingSpotRepository;
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
public class ParkingSpotServiceImpl implements ParkingSpotService{

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final SharedParkingLotRepository sharedParkingLotRepository;
    private final PaymentRecipeRepository paymentRecipeRepository;
    private final JwtService jwtService;


    @Override
    public List<ParkingLotWithTListDTO<ParkingSpotShowDTO>> getAll(HttpServletRequest request) {
        //user id yi çek
        Long userId = getOwnerIdFromRequest(request);

        // user id den owner olduğu parkinglot ların id lerini çek
        List<Long> parkingLotIds = parkingLotRepository.getIds(userId,true);

        // user id den partner olduğu parking lotların id lerini ekle
        parkingLotIds.addAll(sharedParkingLotRepository.getParkingLotIds(userId));

        // dongu kurup  ParkingLotWithTListDTO yu oluştur.
        List<ParkingLotWithTListDTO<ParkingSpotShowDTO>> dtos=new ArrayList<>();
        ParkingLotWithTListDTO<ParkingSpotShowDTO> dto = new ParkingLotWithTListDTO<>();
        for(Long parkingLotId : parkingLotIds){
            dto=parkingLotRepository.getParkingLotWithTListDTO(parkingLotId);
            // dongu içinde! ParkingSpotShowDTO ları oluştur
            dto.setTList(parkingSpotRepository.getAllParkingSpotShowDTO(parkingLotId));
            dtos.add(dto);
        }

        // donguden çıktın dtos u gonder
        return dtos;
    }

    @Override
    public ParkingSpotShowDTO get(HttpServletRequest request, Long id) {
        //girilen id ok?
        ParkingSpot parkingSpot = parkingSpotRepository.getParkingSpotByIdAndEnable(id,true);
        if(parkingSpot==null){
            throw new GlobalRuntimeException("Parking spot not found! ", HttpStatus.BAD_REQUEST);
        }

        // owner/partner ?
        Long userId=getOwnerIdFromRequest(request);
        Long parkingLotId = parkingSpot.getParkingLotId();
        if(!parkingLotRepository.getOwnerIdById(parkingLotId).equals(userId)){
            // owner değil...
            if(sharedParkingLotRepository.checkWithPartnerIdAndParkingLotId(userId,parkingLotId)==null){
                //parnter da değil...
                throw new GlobalRuntimeException("You cant see other users parking spots! ", HttpStatus.UNAUTHORIZED);
            }
        }

        // veriyi gönder
        String tag = parkingSpotRepository.getTag(parkingSpot.getId());
        return new ParkingSpotShowDTO(parkingSpot.getId(),tag,parkingSpot.getName(),parkingSpot.isIndoor(),parkingSpot.getVehicleTypeCode());
    }

    @Override
    public ParkingSpotShowDTO add(HttpServletRequest request, ParkingSpotAddDTO dto) {

        // girilen parking lot id sinde parkinglot varmı?
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(dto.getParkingLotId(),true);
        if(parkingLot==null){
            throw new GlobalRuntimeException("Parking lot not found! ",HttpStatus.NOT_FOUND);
        }

        // owner/partner mı?
        Long userId=getOwnerIdFromRequest(request);
        if(!parkingLot.getOwnerId().equals(userId)){
            // owner değil...
            if(sharedParkingLotRepository.checkWithPartnerIdAndParkingLotId(userId,parkingLot.getId())==null){
                //parnter da değil...
                throw new GlobalRuntimeException("You cant add a parking spot to other users parking lots! ", HttpStatus.UNAUTHORIZED);
            }
        }

        // girilen ödeme tarifesi o otoparka mı ait?
        if(!paymentRecipeRepository.getParkingLotId(dto.getPaymentRecipeId()).equals(dto.getParkingLotId())){
            throw new GlobalRuntimeException("This payment recipe is not belong to this parking lot! ",HttpStatus.BAD_REQUEST);
        }

        // vehicleTypeCode oluştur
        int code=0;
        if(dto.isMOTORCYCLE()){
            code=code|1;
        }if(dto.isCAR()){
            code=code|2;
        }if(dto.isMINIBUS()){
            code=code|4;
        }if(dto.isBUS()){
            code=code|8;
        }if(dto.isTRUCK()){
            code=code|16;
        }

        // ekle
        ParkingSpot parkingSpot = parkingSpotRepository.getParkingSpotByParkingLotIdAndName(dto.getParkingLotId(),dto.getName());
        if(parkingSpot!=null){
            // bu satır var
            if(parkingSpot.isEnable()){
                // satır silinmemiş
                throw new GlobalRuntimeException("This name is already uses! ",HttpStatus.BAD_REQUEST);
            }
        }else{
            parkingSpot = new ParkingSpot();
        }
        parkingSpot.setParkingLotId(dto.getParkingLotId());
        parkingSpot.setName(dto.getName());
        parkingSpot.setPaymentRecipeId(dto.getPaymentRecipeId());
        parkingSpot.setVehicleTypeCode(code);
        parkingSpot.setIndoor(dto.isIndoor());
        parkingSpot.setOccupied(false);
        parkingSpot.setEnable(true);
        parkingSpotRepository.save(parkingSpot);

        // dön
        String tag = parkingSpotRepository.getTag(parkingSpot.getId());
        return new ParkingSpotShowDTO(parkingSpot.getId(),tag,parkingSpot.getName(),parkingSpot.isIndoor(),parkingSpot.getVehicleTypeCode());
    }

    @Override
        public ParkingSpotShowDTO update(HttpServletRequest request, ParkingSpotAddDTO dto, Long id) {

        // parking spot varmı?
        ParkingSpot parkingSpot = parkingSpotRepository.getParkingSpotByIdAndEnable(id,true);
        if(parkingSpot==null){
            throw new GlobalRuntimeException("Parking spot not found! ",HttpStatus.NOT_FOUND);
        }

        // girilen parking lot id sinde parkinglot varmı?
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(dto.getParkingLotId(),true);
        if(parkingLot==null){
            throw new GlobalRuntimeException("Parking lot not found! ",HttpStatus.NOT_FOUND);
        }

        // owner/partner mı?
        Long userId=getOwnerIdFromRequest(request);
        if(!parkingLot.getOwnerId().equals(userId)){
            // owner değil...
            if(sharedParkingLotRepository.checkWithPartnerIdAndParkingLotId(userId,parkingLot.getId())==null){
                //parnter da değil...
                throw new GlobalRuntimeException("You can't update other users parking spot! ", HttpStatus.UNAUTHORIZED);
            }
        }

        // vehicleTypeCode oluştur
        int code=0;
        if(dto.isMOTORCYCLE()){
            code=code|1;
        }if(dto.isCAR()){
            code=code|2;
        }if(dto.isMINIBUS()){
            code=code|4;
        }if(dto.isBUS()){
            code=code|8;
        }if(dto.isTRUCK()){
            code=code|16;
        }

        // ekle
        parkingSpot.setParkingLotId(dto.getParkingLotId());
        parkingSpot.setName(dto.getName());
        parkingSpot.setPaymentRecipeId(dto.getPaymentRecipeId());
        parkingSpot.setVehicleTypeCode(code);
        parkingSpot.setIndoor(dto.isIndoor());
        parkingSpot.setEnable(true);
        parkingSpotRepository.save(parkingSpot);

        // dön
        String tag = parkingSpotRepository.getTag(parkingSpot.getId());
        return new ParkingSpotShowDTO(parkingSpot.getId(),tag,parkingSpot.getName(),parkingSpot.isIndoor(),parkingSpot.getVehicleTypeCode());
    }

    @Override
    public String disable(HttpServletRequest request, Long id) {

        // parking spot varmı?
        ParkingSpot parkingSpot = parkingSpotRepository.getParkingSpotByIdAndEnable(id,true);
        if(parkingSpot==null){
            throw new GlobalRuntimeException("Parking spot not found! ",HttpStatus.NOT_FOUND);
        }

        // owner/partner mı?
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(parkingSpot.getParkingLotId(),true);
        Long userId=getOwnerIdFromRequest(request);
        if(!parkingLot.getOwnerId().equals(userId)){
            // owner değil...
            if(sharedParkingLotRepository.checkWithPartnerIdAndParkingLotId(userId,parkingLot.getId())==null){
                //parnter da değil...
                throw new GlobalRuntimeException("You can't remove other users parking spot! ", HttpStatus.UNAUTHORIZED);
            }
        }

        // şuan kullanımda mı?
        if(parkingSpot.isOccupied()){
            throw new GlobalRuntimeException("You can't remove parking spot when it occupied! ", HttpStatus.UNAUTHORIZED);
        }

        // disable et
        parkingSpot.setEnable(false);
        parkingSpotRepository.save(parkingSpot);
        return "Parking spot removed!";
    }

    @Override
    public ParkingLotWithTListDTO<ParkingSpotDetailDTO> findParkingSpots(Long parkingLotId) {

        // parkingLotId yi kontrol et varmı
        if(parkingLotRepository.getParkingLotByIdAndEnable(parkingLotId,true)==null){
            throw new GlobalRuntimeException("Parking lot not found! ",HttpStatus.NOT_FOUND);
        }

        // parking lot bilgilerini yükle
        ParkingLotWithTListDTO parkingLotWithTListDTO = parkingLotRepository.getParkingLotWithTListDTO(parkingLotId);

        // spotları liste olarak çek
        List<ParkingSpotDetailDTO> dtos = parkingSpotRepository.getAllParkingSpotDetailDTO(parkingLotId);

        // hangi araçların girebileceğini string list e çevir
        int code;
        for(var dto : dtos){
            code = parkingSpotRepository.getVehicleType(dto.getId());
            dto.setCanPark(new ArrayList<>());
            if((code&1)==1){
                dto.canPark("MOTORCYCLE");
            }if((code&2)==2){
                dto.canPark("CAR");
            }if((code&4)==4){
                dto.canPark("MINIBUS");
            }if((code&8)==8){
                dto.canPark("BUS");
            }if((code&16)==16){
                dto.canPark("TRUCK");
            }
        }

        // verileri birleştir göder
        parkingLotWithTListDTO.setTList(dtos);
        return parkingLotWithTListDTO;
    }

    private Long getOwnerIdFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        return jwtService.extractId(token);
    }
}
