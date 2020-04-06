package ua.tqs.airQuality;

import java.util.List;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AirQualityService {
    private int hit=0;
    private int miss=0;
    private int nreq=0;
    private int expiry=20;

    @Autowired
    private AirQualityRepository aqRepo;

    public List<AirQuality> getAll(){
        return aqRepo.findAll();
    }

    public AirQuality getByCityAndCountry(String city, String country) {
        return aqRepo.findByCityAndCountry(city,country);
    }
    public boolean exists(String city,String country) {
        return aqRepo.findByCityAndCountry(city,country) != null;
    }
    public AirQuality save(AirQuality aq) {
        return aqRepo.save(aq);
    }
    public void delete(AirQuality aq){
        aqRepo.delete(aq);
    }
    public void checkAllExpiry(){
        Instant t1=Instant.now();
        List<AirQuality> aqList = this.getAll();
        for (AirQuality aq: aqList){
            if (aq.checkExpiry(t1,expiry)){
                aqRepo.delete(aq);
            }
        }
    }
    public void hit(){
        this.hit+=1;
    }
    public void miss(){
        this.miss+=1;
    }
    public void nreq(){
        this.nreq+=1;
    }
    public int getHits(){
        return this.hit;
    }
    public int getMisses(){
        return this.miss;
    }
    public int getNReq(){
        return this.nreq;
    }

	public void setExpiry(int i) {
        this.expiry=i;
	}


}