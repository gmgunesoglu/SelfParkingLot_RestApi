package com.SoftTech.SelfParkingLot_RestApi.repository;

import com.SoftTech.SelfParkingLot_RestApi.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location,Long> {

    @Query("SELECT DISTINCT l.city FROM Location l WHERE l.enable=true")
    List<String> getCities();

    @Query("SELECT DISTINCT l.town FROM Location l WHERE l.enable=true AND l.city = :city")
    List<String> getTowns(@Param("city") String city);

    @Query("SELECT DISTINCT l.district FROM Location l WHERE l.enable=true AND l.city = :city AND l.town = :town")
    List<String> getDistricts(@Param("city") String city, @Param("town") String town);

    @Query("SELECT  l.id FROM Location l WHERE l.enable=true AND l.city = :city AND l.town = :town AND l.district = :district")
    Long getId(@Param("city") String city, @Param("town") String town, @Param("district") String district);

    Location getLocationsByCityAndTownAndDistrict(String city,String town,String district);

    @Query("SELECT  l.id FROM Location l WHERE l.enable = :enable AND l.city = :city AND l.town = :town AND l.district = :district")
    List<Long> getAllLocationsByCityAndTownAndDistrictAndEnable(String city,String town,String district,Boolean enable);

    @Query("SELECT  l.id FROM Location l WHERE l.enable=:enable AND l.city = :city AND l.town = :town")
    List<Long> getAllLocationIdByCityAndTownAndEnable(String city,String town,Boolean enable);

    @Query("SELECT  l.id FROM Location l WHERE l.enable=:enable AND l.city = :city")
    List<Long> getAllLocationIdByCityAndEnable(String city,Boolean enable);

    @Query("SELECT  l.id FROM Location l WHERE l.enable=:enable")
    List<Long> getAllLocationIdByEnable(Boolean enable);

    List<Location> getAllByEnable(boolean enable);

    Location getLocationByIdAndEnable(Long locationId, boolean enable);
}
