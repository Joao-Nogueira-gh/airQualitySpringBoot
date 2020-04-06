package ua.tqs.airQuality;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AirQualityServiceTest {

    @Mock( lenient = true)
    private AirQualityRepository aqRepository;

    @InjectMocks
    private AirQualityService aqService;

    @BeforeEach
    public void setUp() {
        AirQuality a1 = new AirQuality("Paris","France");
        AirQuality a2 = new AirQuality("Aveiro","Portugal");
        AirQuality a3 = new AirQuality("Kampur","India");

        List<AirQuality> aqList = Arrays.asList(a1, a2, a3);

        Mockito.when(aqRepository.findByCityAndCountry(a1.getCity(),a1.getCountry())).thenReturn(a1);
        Mockito.when(aqRepository.findByCityAndCountry("does not","exist")).thenReturn(null);
        Mockito.when(aqRepository.findAll()).thenReturn(aqList);
    }

    @Test
    public void returnExistentAirQuality() {
        String city="Paris";
        String country="France";
        AirQuality retrievedaq = aqService.getByCityAndCountry(city,country);

        assertThat(retrievedaq.getCity()).isEqualTo(city);
        assertThat(retrievedaq.getCountry()).isEqualTo(country);
    }

    @Test
    public void returnNullIfNotExistent() {
        AirQuality retrievedaq = aqService.getByCityAndCountry("does not","exist");
        assertThat(retrievedaq).isNull();
    }

    @Test
    public void testExistFunctionTrue() {
        String city="Paris";
        String country="France";
        boolean existentAq = aqService.exists(city,country);
        assertThat(existentAq).isEqualTo(true);

        verifyFindByCityAndCountryIsCalledOnce(city, country);
    }

    @Test
    public void testExistFunctionFalse() {
        String city="does not";
        String country="exist";
        boolean existentAq = aqService.exists(city, country);
        assertThat(existentAq).isEqualTo(false);

        verifyFindByCityAndCountryIsCalledOnce(city, country);
    }

    @Test
    public void testSeveralObjects() {
        AirQuality a1 = new AirQuality("Paris","France");
        AirQuality a2 = new AirQuality("Aveiro","Portugal");
        AirQuality a3 = new AirQuality("Kampur","India");

        List<AirQuality> aqList = aqService.getAll();
        verifyFindAllIsCalledOnce();
        assertThat(aqList).hasSize(3).extracting(AirQuality::getId).contains(a1.getId(), a2.getId(), a3.getId());
    }
    @Test
    public void testObjectDeletion(){
        List<AirQuality> aqList=aqService.getAll();
        AirQuality a1=aqList.get(0);

        aqService.delete(a1);
        when(aqService.getByCityAndCountry(a1.getCity(),a1.getCountry())).thenReturn(null);
        assertNull(aqService.getByCityAndCountry(a1.getCity(), a1.getCountry()));
        Mockito.verify(aqRepository, VerificationModeFactory.times(1)).delete(a1);

    }

    private void verifyFindByCityAndCountryIsCalledOnce(String city,String country) {
        Mockito.verify(aqRepository, VerificationModeFactory.times(1)).findByCityAndCountry(city,country);
        Mockito.reset(aqRepository);
    }

    private void verifyFindAllIsCalledOnce() {
        Mockito.verify(aqRepository, VerificationModeFactory.times(1)).findAll();
        Mockito.reset(aqRepository);
    }
}
