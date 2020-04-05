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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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
        when(aqService.getNReq()).thenReturn(1);

        when(aqService.getHits()).thenReturn(0);

        when(aqService.getMisses()).thenReturn(1);

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        when(aqService.getNReq()).thenReturn(2);

        when(aqService.getHits()).thenReturn(1);

        when(aqService.getMisses()).thenReturn(1);

        reset(aqService);
    }

    @Test
    public void testCachingNotPresent() throws Exception {
        String testCity="Paris";
        String testCountry="France";

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        
        when(aqService.getNReq()).thenReturn(1);

        when(aqService.getHits()).thenReturn(0);

        when(aqService.getMisses()).thenReturn(1);

        reset(aqService);
    }

    @Test
    public void testExpiry() throws Exception {
        String testCity="Paris";
        String testCountry="France";

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        
        when(aqService.getNReq()).thenReturn(1);

        when(aqService.getHits()).thenReturn(0);

        when(aqService.getMisses()).thenReturn(1);

        //
        aqService.setExpiry(2);
        Thread.sleep(2000);

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        
        when(aqService.getNReq()).thenReturn(2);

        when(aqService.getHits()).thenReturn(1);

        when(aqService.getMisses()).thenReturn(1);

        mvc.perform(get("/home/api?city="+testCity+"&country="+testCountry).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        
        when(aqService.getNReq()).thenReturn(3);

        when(aqService.getHits()).thenReturn(1);

        when(aqService.getMisses()).thenReturn(2);

        reset(aqService);
    }

}