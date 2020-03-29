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
	public String indexResults(@ModelAttribute AirQuality airQuality, HttpSession session) throws IOException {
		if (airQuality.getCity()=="" || airQuality.getCountry()==""){
			return "invalid input";
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
				return "invalid input";
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
						break;
					case "so2":
						airQuality.setSo2(Double.parseDouble(value));
						break;
					case "no2":
						airQuality.setNo2(Double.parseDouble(value));
						break;
					case "aqi":
						airQuality.setAqi(Integer.parseInt(value));
						break;
					case "co":
						airQuality.setCo(Double.parseDouble(value));
						break;
					case "pm10":
						airQuality.setPm10(Double.parseDouble(value));
						break;
					case "pm25":
						airQuality.setPm25(Double.parseDouble(value));
						break;
				
					default:
						break;
				}
			}
			
			String backsrc=null;
			if (airQuality.getAqi()<100){
				backsrc="/images/nonpol.jpg";
				session.setAttribute("imgsrc", backsrc);
			}
			else{
				backsrc="/images/pol.jpg";
				session.setAttribute("imgsrc", backsrc);
			}
		}
		return "results";
	}

}
