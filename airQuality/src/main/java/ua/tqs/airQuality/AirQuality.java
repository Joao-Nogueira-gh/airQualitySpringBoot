package ua.tqs.airQuality;

import javax.persistence.*;

@Entity
public class AirQuality {
	public AirQuality(){

	}
	public AirQuality(double o3, double so2, double no2, double co, double pm10, double pm25, int aqi) {
		this.o3 = o3;
		this.so2 = so2;
		this.no2 = no2;
		this.co = co;
		this.pm10 = pm10;
		this.pm25 = pm25;
		this.aqi = aqi;
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

	public double getO3() {
		return o3;
	}

	public void setO3(double o3) {
		this.o3 = o3;
	}

	public double getSo2() {
		return so2;
	}

	public void setSo2(double so2) {
		this.so2 = so2;
	}

	public double getNo2() {
		return no2;
	}

	public void setNo2(double no2) {
		this.no2 = no2;
	}

	public double getCo() {
		return co;
	}

	public void setCo(double co) {
		this.co = co;
	}

	public double getPm10() {
		return pm10;
	}

	public void setPm10(double pm10) {
		this.pm10 = pm10;
	}

	public double getPm25() {
		return pm25;
	}

	public void setPm25(double pm25) {
		this.pm25 = pm25;
	}

	public int getAqi() {
		return aqi;
	}

	public void setAqi(int aqi) {
		this.aqi = aqi;
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

	
	
	
}
