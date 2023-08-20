package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.dto.*;
import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.entity.Person;
import com.SoftTech.SelfParkingLot_RestApi.entity.SharedParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.exceptionhandling.GlobalRuntimeException;
import com.SoftTech.SelfParkingLot_RestApi.repository.LocationRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingLotRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.PersonRepository;
import com.SoftTech.SelfParkingLot_RestApi.repository.SharedParkingLotRepository;
import com.SoftTech.SelfParkingLot_RestApi.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.ClientInfoStatus;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotServiveImpl implements ParkingLotService{

    private final ParkingLotRepository parkingLotRepository;
    private final PersonRepository personRepository;
    private final SharedParkingLotRepository sharedParkingLotRepository;
    private final LocationRepository locationRepository;
    private final JwtService jwtService;


    @Override
    public ParkingLotListDTO getAll(HttpServletRequest request) {
        Long ownerId = getOwnerIdFromRequest(request);
        ParkingLotListDTO dto = new ParkingLotListDTO();
        List<ParkingLot> myParkingLots = parkingLotRepository.findByOwnerIdAndEnable(ownerId,true);
//        List<ParkingLot> otherParkingLots = new ArrayList<>();
        List<ParkingLotPartnerDTO> underMyRule = new ArrayList<>();
        List<ParkingLotOwnerDTO> upperMyRule = new ArrayList<>();
        for(ParkingLot myParkingLot : myParkingLots){
            List<PersonPartnerDTO> partners = new ArrayList<>();
            List<Long> partnerIds = sharedParkingLotRepository.getPartnerIds(myParkingLot.getId());
            for(Long partnerId : partnerIds){
                Person person = personRepository.getPersonByIdAndEnable(partnerId,true);
                partners.add(PersonPartnerDTO.builder()
                        .firstName(person.getFirstName())
                        .lastName(person.getLastName())
                        .userName(person.getUsername())
                        .email(person.getEmail())
                        .phoneNumber(person.getPhoneNumber()).build());
            }
            Location location = locationRepository.getLocationByIdAndEnable(myParkingLot.getLocationId(),true);
            underMyRule.add(ParkingLotPartnerDTO.builder()
                    .partners(partners)
                    .id(myParkingLot.getId())
                    .name(myParkingLot.getName())
                    .city(location.getCity())
                    .town(location.getTown())
                    .district(location.getDistrict())
                    .address(myParkingLot.getAddress()).build());
        }
        List<Long> otherParkingLotsIds = sharedParkingLotRepository.getParkingLotIds(ownerId);
        for (Long id : otherParkingLotsIds){
            ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(id,true);
            Person owner = personRepository.getPersonByIdAndEnable(parkingLot.getOwnerId(),true);
            Location location = locationRepository.getLocationByIdAndEnable(parkingLot.getLocationId(),true);
            upperMyRule.add(ParkingLotOwnerDTO.builder()
                    .owner(PersonPartnerDTO.builder()
                            .firstName(owner.getFirstName())
                            .lastName(owner.getLastName())
                            .userName(owner.getUsername())
                            .email(owner.getEmail())
                            .phoneNumber(owner.getPhoneNumber()).build())
                    .Id(parkingLot.getId())
                    .name(parkingLot.getName())
                    .city(location.getCity())
                    .town(location.getTown())
                    .district(location.getDistrict())
                    .address(parkingLot.getAddress()).build());
        }
        dto.setUnderMyRule(underMyRule);
        dto.setUpperMyRule(upperMyRule);
        return dto;
    }

    @Override
    public ParkingLot get(HttpServletRequest request, Long id) {
        Long ownerId = getOwnerIdFromRequest(request);
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(id,true);
        if(parkingLot == null){
            throw new GlobalRuntimeException("Parking lot not found! ",HttpStatus.NOT_FOUND);
        }
        if(parkingLot.getOwnerId().equals(ownerId)){
            return parkingLot;
        }
        // kendisinin değil
        SharedParkingLot sharedParkingLot = sharedParkingLotRepository.getSharedParkingLotByParkingLotIdAndPartnerId(id,ownerId);
        if(sharedParkingLot != null){
            // ortak
            return parkingLot;
        }
        // ortak değil
        throw new GlobalRuntimeException("You can't access other users parking lot! ",HttpStatus.UNAUTHORIZED);
    }

    @Override
    public ParkingLot add(HttpServletRequest request, ParkingLotDTO dto) {
        Long locationId = getLocationId(dto.getCity(),dto.getTown(),dto.getDistrict());
        if(locationId==null){
            throw new GlobalRuntimeException("Location not found! ", HttpStatus.NOT_FOUND);
        }
        Long ownerId = getOwnerIdFromRequest(request);
        ParkingLot parkingLot=new ParkingLot();
        parkingLot.setAddress(dto.getAddress());
        parkingLot.setName(dto.getName());
        parkingLot.setOwnerId(ownerId);
        parkingLot.setLocationId(locationId);
        parkingLot.setEnable(true);
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public ParkingLot update(HttpServletRequest request, ParkingLotDTO dto, Long id) {
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(id,true);
        if(parkingLot == null){
            throw new GlobalRuntimeException("ParkingLot not found! ", HttpStatus.NOT_FOUND);
        }
        Long locationId = getLocationId(dto.getCity(),dto.getTown(),dto.getDistrict());
        if(locationId==null){
            throw new GlobalRuntimeException("Location not found! ", HttpStatus.NOT_FOUND);
        }
        Long ownerId = getOwnerIdFromRequest(request);
        if(!ownerId.equals(parkingLot.getOwnerId())){
            throw new GlobalRuntimeException("You can't update other users parking lot! ", HttpStatus.UNAUTHORIZED);
        }
        parkingLot.setAddress(dto.getAddress());
        parkingLot.setName(dto.getName());
        parkingLot.setLocationId(locationId);
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public String disable(HttpServletRequest request, Long id) {
        // id deki otopark varmı
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(id,true);
        if(parkingLot == null){
            throw new GlobalRuntimeException("ParkingLot not found! ", HttpStatus.NOT_FOUND);
        }
        // otoparkın sahibimi
        Long ownerId = getOwnerIdFromRequest(request);
        if(!ownerId.equals(parkingLot.getOwnerId())){
            throw new GlobalRuntimeException("You can't share other users parking lot! ", HttpStatus.UNAUTHORIZED);
        }
        // otoparkın ortakları varmı
        List<Long> partnerIds = sharedParkingLotRepository.getPartnerIds(id);
        if(partnerIds.size()>0){
            throw new GlobalRuntimeException("You have to remove partners before remove parking lot! ", HttpStatus.UNAUTHORIZED);
        }
        // sil...
        parkingLot.setEnable(false);
        parkingLotRepository.save(parkingLot);
        return null;
    }

    @Override
    public PersonPartnerDTO openShare(HttpServletRequest request, ParkingLotShareDTO dto, Long id) {
        // parkinglot id varmı kontrol et
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(id,true);
        if(parkingLot == null){
            throw new GlobalRuntimeException("ParkingLot not found! ", HttpStatus.NOT_FOUND);
        }
        // parkinglot kendisine mi ait kontrol et
        Long ownerId = getOwnerIdFromRequest(request);
        if(!ownerId.equals(parkingLot.getOwnerId())){
            throw new GlobalRuntimeException("You can't open share other users parking lot! ", HttpStatus.UNAUTHORIZED);
        }
        // partner kontrol et
        Person partner = personRepository.getPersonByUsernameAndEnable(dto.getSharedUsername(),true);
        if(partner==null){
            throw new GlobalRuntimeException("Partner not found! ",HttpStatus.NOT_FOUND);
        }
        if(!partner.getSecretKey().equals(dto.getSecretKey())){
            throw new GlobalRuntimeException("Secret key incorrect! ",HttpStatus.UNAUTHORIZED);
        }
        //zaten paylaşımda mı kontrol et
        SharedParkingLot sharedParkingLot = sharedParkingLotRepository.getSharedParkingLotByParkingLotIdAndPartnerId(id,partner.getId());
        if(sharedParkingLot != null){
            throw new GlobalRuntimeException("This user is already your partner! ",HttpStatus.BAD_REQUEST);
        }
        // paylaşımı aç
        sharedParkingLot = new SharedParkingLot();
        sharedParkingLot.setParkingLotId(id);
        sharedParkingLot.setPartnerId(partner.getId());
        sharedParkingLotRepository.save(sharedParkingLot);
        // partnerın bilgilerini döndür
        return PersonPartnerDTO.builder()
                .userName(partner.getUsername())
                .firstName(partner.getFirstName())
                .lastName(partner.getLastName())
                .phoneNumber(partner.getPhoneNumber())
                .email(partner.getEmail()).build();
    }

    @Override
    public PersonPartnerDTO closeShare(HttpServletRequest request, ParkingLotCloseShareDTO dto, Long id) {
        // parkinglot id varmı kontrol et
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(id,true);
        if(parkingLot == null){
            throw new GlobalRuntimeException("ParkingLot not found! ", HttpStatus.NOT_FOUND);
        }
        // parkinglot kendisine mi ait kontrol et
        Long ownerId = getOwnerIdFromRequest(request);
        if(!ownerId.equals(parkingLot.getOwnerId())){
            SharedParkingLot sharedParkingLot = sharedParkingLotRepository.getSharedParkingLotByParkingLotIdAndPartnerId(id,ownerId);
            if(sharedParkingLot==null){
                // owner değil ortak da değil
                throw new GlobalRuntimeException("You can't close share other users parking lot! ", HttpStatus.UNAUTHORIZED);
            }
            // ortak
            sharedParkingLotRepository.delete(sharedParkingLot);
            Person person = personRepository.findById(parkingLot.getOwnerId()).get();
            return PersonPartnerDTO.builder()
                    .userName(person.getUsername())
                    .firstName(person.getFirstName())
                    .lastName(person.getLastName())
                    .phoneNumber(person.getPhoneNumber())
                    .email(person.getEmail()).build();
        }
        // partner kontrol et
        Person partner = personRepository.getPersonByUsernameAndEnable(dto.getSharedUsername(),true);
        if(partner==null){
            throw new GlobalRuntimeException("Partner not found! ",HttpStatus.NOT_FOUND);
        }
        // paylaşımda mı kontrol et
        SharedParkingLot sharedParkingLot = sharedParkingLotRepository.getSharedParkingLotByParkingLotIdAndPartnerId(id,partner.getId());
        if(sharedParkingLot == null){
            throw new GlobalRuntimeException("This user isn't already your partner! ",HttpStatus.BAD_REQUEST);
        }
        // paylaşımı kapat
        sharedParkingLotRepository.delete(sharedParkingLot);
        // eski partnerın bilgilerini döndür
        return PersonPartnerDTO.builder()
                .userName(partner.getUsername())
                .firstName(partner.getFirstName())
                .lastName(partner.getLastName())
                .phoneNumber(partner.getPhoneNumber())
                .email(partner.getEmail()).build();
    }

    @Override
    public String abdicate(HttpServletRequest request, ParkingLotShareDTO dto, Long id) {
        // parkinglot id varmı kontrol et
        ParkingLot parkingLot = parkingLotRepository.getParkingLotByIdAndEnable(id,true);
        if(parkingLot == null){
            throw new GlobalRuntimeException("ParkingLot not found! ", HttpStatus.NOT_FOUND);
        }
        // parkinglot kendisine mi ait kontrol et
        Long ownerId = getOwnerIdFromRequest(request);
        if(!ownerId.equals(parkingLot.getOwnerId())){
            throw new GlobalRuntimeException("You can't abdicate other users parking lot! ", HttpStatus.UNAUTHORIZED);
        }
        // partner kontrol et
        Person partner = personRepository.getPersonByUsernameAndEnable(dto.getSharedUsername(),true);
        if(partner==null){
            throw new GlobalRuntimeException("Partner not found! ",HttpStatus.NOT_FOUND);
        }
        if(!partner.getSecretKey().equals(dto.getSecretKey())){
            throw new GlobalRuntimeException("Secret key incorrect! ",HttpStatus.UNAUTHORIZED);
        }
        // paylaşımda mı kontrol et
        SharedParkingLot sharedParkingLot = sharedParkingLotRepository.getSharedParkingLotByParkingLotIdAndPartnerId(id,partner.getId());
        if(sharedParkingLot == null){
            throw new GlobalRuntimeException("This user isn't your partner! ",HttpStatus.BAD_REQUEST);
        }
        // owner ile partner ı değiştir
        parkingLot.setOwnerId(partner.getId());
        sharedParkingLotRepository.delete(sharedParkingLot);
        sharedParkingLot.setPartnerId(ownerId);
        sharedParkingLotRepository.save(sharedParkingLot);
        parkingLotRepository.save(parkingLot);
        return "You abdicated your parking lot to "+partner.getUsername();
    }

    private Long getOwnerIdFromRequest(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token=token.substring(7);
        return jwtService.extractId(token);
    }

    private Long getLocationId(String city,String town,String district){
        return locationRepository.getId(city.toUpperCase(),town.toUpperCase(),district.toUpperCase());
    }
}
