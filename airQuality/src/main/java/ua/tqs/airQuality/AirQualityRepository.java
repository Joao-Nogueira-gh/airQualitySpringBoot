package ua.tqs.airQuality;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AirQualityRepository extends JpaRepository<AirQuality, Long> {

    public AirQuality findByCityAndCountry(String city, String country);
    public List<AirQuality> findAll();

}