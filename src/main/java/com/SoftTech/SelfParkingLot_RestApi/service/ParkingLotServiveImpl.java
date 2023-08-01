package com.SoftTech.SelfParkingLot_RestApi.service;

import com.SoftTech.SelfParkingLot_RestApi.entity.ParkingLot;
import com.SoftTech.SelfParkingLot_RestApi.repository.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingLotServiveImpl implements ParkingLotService{

    private final ParkingLotRepository parkingLotRepository;
    private final LocationService locationService;


    @Override
    public List<ParkingLot> getAll() {
        return parkingLotRepository.findAll();
    }

    @Override
    public ParkingLot get(Long id) {
        return parkingLotRepository.findById(id).get();
    }

    @Override
    public ParkingLot save(ParkingLot parkingLot) {
        if(parkingLot.getLocation().getId()!=null){
            if(parkingLot.getLocation().getId()!=0){
                parkingLot.setLocation(locationService.get(parkingLot.getLocation().getId()));
            }
        }
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public ParkingLot delete(Long id) {
        ParkingLot parkingLot = parkingLotRepository.findById(id).get();
        parkingLotRepository.delete(parkingLot);
        return parkingLot;
    }

    @Override
    public ParkingLot update(ParkingLot parkingLot, Long id) {
        ParkingLot updatedParkingLot = parkingLotRepository.findById(id).get();
        updatedParkingLot.setName(parkingLot.getName());
            updatedParkingLot.setLocation(locationService.get(parkingLot.getLocation().getId()));
        return parkingLotRepository.save(updatedParkingLot);
    }
}
