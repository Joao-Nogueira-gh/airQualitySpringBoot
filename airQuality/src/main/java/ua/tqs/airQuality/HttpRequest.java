package ua.tqs.airQuality;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HttpRequest {
    private HttpURLConnection con;

    public HttpRequest(ArrayList<String> list) throws IOException {
        String url = "https://api.weatherbit.io/v2.0/current/airquality";
        url += "?city=" + list.get(0) + "&country=" + list.get(1);
        url += "&key=aedcc0288929405daccb9096bbd743fd";
        URL thisurl = new URL(url);
        con = (HttpURLConnection) thisurl.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);

    }

    public String getJson() throws IOException {
        int responsecode = con.getResponseCode();
        con.connect();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        con.disconnect();
        return Integer.toString(responsecode)+"next"+response.toString();
    }

}