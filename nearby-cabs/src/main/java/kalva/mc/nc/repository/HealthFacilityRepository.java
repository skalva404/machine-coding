package kalva.mc.nc.repository;

import kalva.mc.nc.entity.HealthFacility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HealthFacilityRepository extends JpaRepository<HealthFacility, Integer> {

    @Query(value = "SELECT nhf.id, nhf.name, nhf.geom "
            + "FROM health_facilities nhf, counties nsc "
            + "WHERE ST_Within(nhf.geom, nsc.geom) AND nsc.id= :subCountyId"
            , nativeQuery = true)
    List<HealthFacility> findAllHospitalsWithinSubCounty(@Param("subCountyId") int subCountyId);

    @Query(value = "SELECT nhf.id, nhf.name, nhf.geom, ST_Distance(nhf.geom,ST_SetSRID(ST_Point(:userLongitude,:userLatitude),4326)) AS distance "
            + "FROM health_facilities nhf "
            + "ORDER BY nhf.geom  <-> ST_SetSRID(ST_Point(:userLongitude,:userLatitude),4326) "
            + "LIMIT 5"
            , nativeQuery = true)
    List<HealthFacility> findAllHospitalsByDistanceFromUser(@Param("userLongitude") Double userLongitude, @Param("userLatitude") Double userLatitude);

}