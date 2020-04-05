package ua.tqs.airQuality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class AirQualityController {

	@Autowired
    private AirQualityService aqService;

	@GetMapping("/home")
	public String index(Model model) {
		model.addAttribute("AirQuality", new AirQuality());
		return "index";
	}
	@GetMapping("/home/api/stats")
	@ResponseBody
	public HashMap<String,Integer> getCacheStats(){
		HashMap<String,Integer> stats=new HashMap<>();
		stats.put("Number of requests", aqService.getNReq());
		stats.put("Hits", aqService.getHits());
		stats.put("Misses", aqService.getMisses());
		return stats;
		
	}

	@GetMapping("/home/api")
	@ResponseBody
	public AirQuality getAirQuality(@RequestParam(value = "city", defaultValue = "") String city, @RequestParam(value = "country", defaultValue = "") String country)
			throws IOException {
		if (city=="" || country=="" || city==null || country==null || city.length()==0 || country.length()==0){
			return new AirQuality();
		}
		if (aqService.exists(city, country)){
			AirQuality cachedaq = aqService.getByCityAndCountry(city, country);
			aqService.hit();
			aqService.nreq();
			aqService.checkAllExpiry();
			return cachedaq;
		}
		else{
			ArrayList<String> l=new ArrayList<>();
			l.add(city);
			l.add(country);
			HttpRequest req=new HttpRequest(l);
			String response=req.getJson();
			if (response!=null){
				String[] values = response.split("next");
				int code=Integer.parseInt(values[0]);
				if (code==200){
					;
				}
				else if (code==204){
					return new AirQuality();
				}
				System.out.println(values[1]);
				JsonParser jp=JsonParserFactory.getJsonParser();
				Map<String, Object> map = jp.parseMap(values[1]);
				String data = map.get("data").toString();
				data=data.substring(2,data.length()-2);
				String[] fields = data.trim().split(",");

				AirQuality aq=new AirQuality();
				aq.setCity(city);
				aq.setCountry(country);
				
				for (String s:fields){
					s=s.trim();
					String[] pairs = s.split("=");
					String key=pairs[0];
					String value=pairs[1];
					switch (key) {
						case "o3":
							aq.setO3(Double.parseDouble(value));
							break;
						case "so2":
							aq.setSo2(Double.parseDouble(value));
							break;
						case "no2":
							aq.setNo2(Double.parseDouble(value));
							break;
						case "aqi":
							aq.setAqi(Integer.parseInt(value));
							break;
						case "co":
							aq.setCo(Double.parseDouble(value));
							break;
						case "pm10":
							aq.setPm10(Double.parseDouble(value));
							break;
						case "pm25":
							aq.setPm25(Double.parseDouble(value));
							break;
					
						default:
							break;
					}
				}
				aqService.save(aq);
				aqService.miss();
				aqService.nreq();
				aqService.checkAllExpiry();
				return aq;
			}
		}
		
		return new AirQuality();
	}


	@PostMapping("/home/results")
	public String indexResults(@ModelAttribute AirQuality airQuality, Model extra) throws IOException {
		if (airQuality.getCity()=="" || airQuality.getCountry()==""){
			return "error";
		}
		if (aqService.exists(airQuality.getCity(), airQuality.getCountry())){
			AirQuality cachedaq = aqService.getByCityAndCountry(airQuality.getCity(), airQuality.getCountry());
			aqService.hit();
			aqService.nreq();
			//
			airQuality.setO3(cachedaq.getO3());
			airQuality.setSo2(cachedaq.getSo2());
			airQuality.setNo2(cachedaq.getNo2());
			airQuality.setAqi(cachedaq.getAqi());
			airQuality.setCo(cachedaq.getCo());
			airQuality.setPm10(cachedaq.getPm10());
			airQuality.setPm25(cachedaq.getPm25());
			//
			extra.addAttribute("o3", airQuality.evalO3());
			extra.addAttribute("so2", airQuality.evalSO2());
			extra.addAttribute("no2", airQuality.evalNO2());
			extra.addAttribute("aqi", airQuality.evalAQI());
			extra.addAttribute("co", airQuality.evalCO());
			extra.addAttribute("pm10", airQuality.evalPM10());
			extra.addAttribute("pm25", airQuality.evalPM25());
			//
			if (airQuality.getAqi()<100){
				extra.addAttribute("bgimg", "0");
			}
			else{
				extra.addAttribute("bgimg", "1");
			}
			extra.addAttribute("nreq", aqService.getNReq());
			extra.addAttribute("hits", aqService.getHits());
			extra.addAttribute("misses", aqService.getMisses());
			aqService.checkAllExpiry();
			return "results";
		}
		else{
			ArrayList<String> l=new ArrayList<>();
			l.add(airQuality.getCity());
			l.add(airQuality.getCountry());
			HttpRequest req=new HttpRequest(l);
			String response=req.getJson();
			if (response!=null){
				String[] values = response.split("next");
				int code=Integer.parseInt(values[0]);
				if (code==200){
					;
				}
				else if (code==204){
					return "error";
				}
				aqService.miss();
				aqService.nreq();
				System.out.println(values[1]);
				JsonParser jp=JsonParserFactory.getJsonParser();
				Map<String, Object> map = jp.parseMap(values[1]);
				String data = map.get("data").toString();
				data=data.substring(2,data.length()-2);
				String[] fields = data.trim().split(",");
				
				for (String s:fields){
					s=s.trim();
					String[] pairs = s.split("=");
					String key=pairs[0];
					String value=pairs[1];
					switch (key) {
						case "o3":
							airQuality.setO3(Double.parseDouble(value));
							extra.addAttribute("o3", airQuality.evalO3());
							break;
						case "so2":
							airQuality.setSo2(Double.parseDouble(value));
							extra.addAttribute("so2", airQuality.evalSO2());
							break;
						case "no2":
							airQuality.setNo2(Double.parseDouble(value));
							extra.addAttribute("no2", airQuality.evalNO2());
							break;
						case "aqi":
							airQuality.setAqi(Integer.parseInt(value));
							extra.addAttribute("aqi", airQuality.evalAQI());
							break;
						case "co":
							airQuality.setCo(Double.parseDouble(value));
							extra.addAttribute("co", airQuality.evalCO());
							break;
						case "pm10":
							airQuality.setPm10(Double.parseDouble(value));
							extra.addAttribute("pm10", airQuality.evalPM10());
							break;
						case "pm25":
							airQuality.setPm25(Double.parseDouble(value));
							extra.addAttribute("pm25", airQuality.evalPM25());
							break;
					
						default:
							break;
					}
					if (airQuality.getAqi()<100){
						extra.addAttribute("bgimg", "0");
					}
					else{
						extra.addAttribute("bgimg", "1");
					}
					extra.addAttribute("nreq", aqService.getNReq());
					extra.addAttribute("hits", aqService.getHits());
					extra.addAttribute("misses", aqService.getMisses());
				}
				aqService.save(airQuality);
				aqService.checkAllExpiry();
				return "results";
			}
			return "results";
		}
	}

}