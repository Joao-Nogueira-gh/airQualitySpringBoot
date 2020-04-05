package ua.tqs.airQuality;

import javax.persistence.*;
import java.time.Duration;
import java.time.Instant;

@Entity
public class AirQuality {
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
			return "very good";
		}
		else if (o3<=120){
			return "good";
		}
		else if (o3<=180){
			return "medium";
		}
		else if (o3<=240){
			return "bad";
		}
		else {
			return "very bad";
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
			return "very good";
		}
		else if (so2<=200){
			return "good";
		}
		else if (so2<=350){
			return "medium";
		}
		else if (so2<=500){
			return "bad";
		}
		else {
			return "very bad";
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
			return "very good";
		}
		else if (no2<=100){
			return "good";
		}
		else if (no2<=200){
			return "medium";
		}
		else if (no2<=400){
			return "bad";
		}
		else {
			return "very bad";
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
			return "very good";
		}
		else if (co<=139){
			return "good";
		}
		else if (co<=199){
			return "medium";
		}
		else if (co<=399){
			return "bad";
		}
		else {
			return "very bad";
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
			return "very good";
		}
		else if (pm10<=30){
			return "good";
		}
		else if (pm10<=50){
			return "medium";
		}
		else if (pm10<=100){
			return "bad";
		}
		else {
			return "very bad";
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
			return "very good";
		}
		else if (pm25<=20){
			return "good";
		}
		else if (pm25<=30){
			return "medium";
		}
		else if (pm25<=60){
			return "bad";
		}
		else {
			return "very bad";
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
			return "good";
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
	public boolean checkExpiry(Instant t2){
		System.out.println(this.timestamp+"-"+t2);
		Duration between=Duration.between(this.timestamp, t2);
		long secs = between.getSeconds();
		return (secs>20) ? true : false ;
	}

	public Long getId() {
		return id;
	}

	
	
	
}
