package ua.tqs.airQuality;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
public class AirQualityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AirQualityRepository aqRepository;

    @Test
    public void findByCityAndCountry_return() {
        AirQuality pf = new AirQuality("Paris","France");
        entityManager.persistAndFlush(pf);

        AirQuality retrievedAq = aqRepository.findByCityAndCountry(pf.getCity(),pf.getCountry());
        assertEquals(retrievedAq.getId(),pf.getId());
    }

    @Test
    public void ifDoesntExist_returnNull() {
        AirQuality retrievedAq = aqRepository.findByCityAndCountry("a","b");
        assertNull(retrievedAq);
    }

    @Test
    public void testFindAll() {
        AirQuality a1 = new AirQuality("Paris","France");
        AirQuality a2 = new AirQuality("Aveiro","Portugal");
        AirQuality a3 = new AirQuality("Kampur","India");

        entityManager.persist(a1);
        entityManager.persist(a2);
        entityManager.persist(a3);
        entityManager.flush();

        List<AirQuality> aqList = aqRepository.findAll();

        assertThat(aqList).hasSize(3).extracting(AirQuality::getCity).containsOnly(a1.getCity(), a2.getCity(), a3.getCity());
    }
}