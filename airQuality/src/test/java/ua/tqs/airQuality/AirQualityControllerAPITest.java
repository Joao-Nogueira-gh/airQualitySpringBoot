package ua.tqs.airQuality;

import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AirQualityController.class)
public class AirQualityControllerAPITest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AirQualityService aqService;

    @BeforeEach
    public void setUp() throws Exception {
        AirQuality a1 = new AirQuality("Paris","France");
        AirQuality a2 = new AirQuality("Aveiro","Portugal");
        AirQuality a3 = new AirQuality("Kampur","India");

        aqService.save(a1);
        aqService.save(a2);
        aqService.save(a3);
    }

    @Test
    public void verifyJsonGetWhenExists() throws Exception {
        String testCity="Paris";
        String testCountry="France";

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.city", is(testCity))).andExpect(jsonPath("$.country", is(testCountry)));
        reset(aqService);
    }

    @Test
    public void verifyJsonGetWhenDoesntExist() throws Exception {
        String testCity="doesNot";
        String testCountry="exist";

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.city").value(IsNull.nullValue())).andExpect(jsonPath("$.country").value(IsNull.nullValue()));
        reset(aqService);
    }

    @Test
    public void testCachingPresent() throws Exception {
        String testCity="Paris";
        String testCountry="France";

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        given(aqService.exists(testCity, testCountry)).willReturn(true);
        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        
        Mockito.verify(aqService, VerificationModeFactory.times(2)).nreq();
        Mockito.verify(aqService, VerificationModeFactory.times(1)).hit();
        Mockito.verify(aqService, VerificationModeFactory.times(1)).miss();

        Mockito.verify(aqService, VerificationModeFactory.times(2)).checkAllExpiry();

        reset(aqService);
    }

    @Test
    public void testCachingNotPresent() throws Exception {
        String testCity="Paris";
        String testCountry="France";

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        
        Mockito.verify(aqService, VerificationModeFactory.times(1)).nreq();
        Mockito.verify(aqService, VerificationModeFactory.times(0)).hit();
        Mockito.verify(aqService, VerificationModeFactory.times(1)).miss();

        reset(aqService);
    }

    @Test
    public void testExpiry() throws Exception {
        String testCity="Paris";
        String testCountry="France";

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        //assume passage of time (ttl)
        given(aqService.exists(testCity, testCountry)).willReturn(true);
        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        given(aqService.exists(testCity, testCountry)).willReturn(false);
        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        
        Mockito.verify(aqService, VerificationModeFactory.times(3)).nreq();
        Mockito.verify(aqService, VerificationModeFactory.times(1)).hit();
        Mockito.verify(aqService, VerificationModeFactory.times(2)).miss();

        Mockito.verify(aqService, VerificationModeFactory.times(3)).checkAllExpiry();

        reset(aqService);
    }

}