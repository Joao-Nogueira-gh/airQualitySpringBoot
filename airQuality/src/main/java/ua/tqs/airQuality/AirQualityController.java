package ua.tqs.airQuality;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AirQualityController {

	@GetMapping("/home")
	public String index(Model model) {
		model.addAttribute("AirQuality", new AirQuality());
		return "index";
	}

	@PostMapping("/home/results")
	public String indexResults(@ModelAttribute AirQuality airQuality, Model extra) throws IOException {
		if (airQuality.getCity()=="" || airQuality.getCountry()==""){
			return "error";
		}
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
			}
			return "results";
		}
		return "results";
	}

}
