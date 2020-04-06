package ua.tqs.airQuality;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Entity
public class AirQuality {
	private static final String VERYGOOD = "very good";
	private static final String GOOD = "good";
	private static final String MEDIUM = "medium";
	private static final String BAD = "bad";
	private static final String VERYBAD = "very bad";

	public AirQuality(){
		this.timestamp=Instant.now();
	}
	public AirQuality(String city, String country) {
		this.city = city;
		this.country = country;
		this.timestamp = Instant.now();
	}
	public AirQuality(double o3, double so2, double no2, double co, double pm10, double pm25, int aqi) {
		this.o3 = o3;
		this.so2 = so2;
		this.no2 = no2;
		this.co = co;
		this.pm10 = pm10;
		this.pm25 = pm25;
		this.aqi = aqi;
		this.timestamp=Instant.now();
	}
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private double o3;
	
	private double so2;

	private double no2;

	private double co;

	private double pm10;

	private double pm25;

	private int aqi;

	private String city;

	private String country;

	private Instant timestamp;

	public double getO3() {
		return o3;
	}

	public void setO3(double o3) {
		this.o3 = o3;
	}
	public String evalO3(){
		if (o3<=60){
			return VERYGOOD;
		}
		else if (o3<=120){
			return GOOD;
		}
		else if (o3<=180){
			return MEDIUM;
		}
		else if (o3<=240){
			return BAD;
		}
		else {
			return VERYBAD;
		}
	}
	
	public double getSo2() {
		return so2;
	}
	
	public void setSo2(double so2) {
		this.so2 = so2;
	}
	public String evalSO2(){
		if (so2<=100){
			return VERYGOOD;
		}
		else if (so2<=200){
			return GOOD;
		}
		else if (so2<=350){
			return MEDIUM;
		}
		else if (so2<=500){
			return BAD;
		}
		else {
			return VERYBAD;
		}
	}
	
	public double getNo2() {
		return no2;
	}
	
	public void setNo2(double no2) {
		this.no2 = no2;
	}
	public String evalNO2(){
		if (no2<=50){
			return VERYGOOD;
		}
		else if (no2<=100){
			return GOOD;
		}
		else if (no2<=200){
			return MEDIUM;
		}
		else if (no2<=400){
			return BAD;
		}
		else {
			return VERYBAD;
		}
	}
	
	public double getCo() {
		return co;
	}
	
	public void setCo(double co) {
		this.co = co;
	}
	public String evalCO(){
		if (co<=99){
			return VERYGOOD;
		}
		else if (co<=139){
			return GOOD;
		}
		else if (co<=199){
			return MEDIUM;
		}
		else if (co<=399){
			return BAD;
		}
		else {
			return VERYBAD;
		}
	}
	
	public double getPm10() {
		return pm10;
	}
	
	public void setPm10(double pm10) {
		this.pm10 = pm10;
	}
	public String evalPM10(){
		if (pm10<=15){
			return VERYGOOD;
		}
		else if (pm10<=30){
			return GOOD;
		}
		else if (pm10<=50){
			return MEDIUM;
		}
		else if (pm10<=100){
			return BAD;
		}
		else {
			return VERYBAD;
		}
	}
	
	public double getPm25() {
		return pm25;
	}
	
	public void setPm25(double pm25) {
		this.pm25 = pm25;
	}
	public String evalPM25(){
		if (pm25<=10){
			return VERYGOOD;
		}
		else if (pm25<=20){
			return GOOD;
		}
		else if (pm25<=30){
			return MEDIUM;
		}
		else if (pm25<=60){
			return BAD;
		}
		else {
			return VERYBAD;
		}
	}
	
	public int getAqi() {
		return aqi;
	}
	
	public void setAqi(int aqi) {
		this.aqi = aqi;
	}
	public String evalAQI(){
		if (aqi<=50){
			return GOOD;
		}
		else if (aqi<=100){
			return "moderate";
		}
		else if (aqi<=150){
			return "unhealthy for sensitive groups";
		}
		else if (aqi<=200){
			return "unhealthy";
		}
		else if (aqi<=300){
			return "very unhealthy";
		}
		else {
			return "hazardous";
		}
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	public boolean checkExpiry(Instant t2, int expiry){
		Duration between=Duration.between(this.timestamp, t2);
		long secs = between.getSeconds();
		return (secs>expiry) ;
	}

	public Long getId() {
		return id;
	}

	
	
	
}
