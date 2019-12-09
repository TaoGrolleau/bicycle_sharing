package spring.main.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.main.models.BicycleStation;
import spring.main.repositories.BicycleStationRepository;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

@Controller
public class UpdateController {
    @Autowired
    BicycleStationRepository bicycleStationRepository;

    @RequestMapping("/update")
    public String update_rdf() throws IOException, ParseException {
        bicycleStationRepository.deleteAll();

        get_data("Rennes");
        get_data("Paris");
        get_data("Lyon");

        return "redirect:/";
    }

    public void get_data(String city) throws IOException, ParseException {
        String inline = "";
        URL url;
        switch (city) {
            case "Rennes":
                url = new URL("https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=etat-des-stations-le-velo-star-en-temps-reel");
            case "Lyon":
                url = new URL("https://download.data.grandlyon.com/wfs/rdata?SERVICE=WFS&VERSION=1.1.0&outputformat=GEOJSON&request=GetFeature&typename=jcd_jcdecaux.jcdvelov&SRSNAME=urn:ogc:def:crs:EPSG::4171");
            case "Paris":
                url = new URL("https://opendata.paris.fr/api/records/1.0/search/?dataset=velib-disponibilite-en-temps-reel&facet=overflowactivation&facet=creditcard&facet=kioskstate&facet=station_state");
            default:
                url = new URL("");
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();
        int responsecode = conn.getResponseCode();

        if (responsecode != 200) {
            throw new RuntimeException("HttpResponseCode: " + responsecode);
        } else {
            Scanner sc = new Scanner(url.openStream());

            while (sc.hasNext()) {
                inline += sc.nextLine();
            }
            System.out.println("\nJSON data in string format");
            System.out.println(inline);

            sc.close();
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jobj = (JSONObject) jsonParser.parse(inline);
        JSONArray jarray = (JSONArray) jobj.get("records");

        switch (city) {
            case "Rennes":
                for (int i = 0; i < jarray.size(); i++) {
                    JSONObject json_obj_1 = (JSONObject) jarray.get(i);
                    JSONObject fields = (JSONObject) json_obj_1.get("fields");
                    JSONArray coordonnees = (JSONArray) fields.get("coordonnees");

                    BicycleStation bicycleStationToAdd = new BicycleStation(
                            fields.get("nom").toString(),
                            fields.get("idstation").toString(),
                            coordonnees.get(1).toString(),
                            coordonnees.get(0).toString(),
                            fields.get("nombrevelosdisponibles").toString(),
                            fields.get("nombreemplacementsdisponibles").toString(),
                            fields.get("nombreemplacementsactuels").toString());
                    System.out.println(bicycleStationToAdd);
                    bicycleStationRepository.save(bicycleStationToAdd);
                }
            case "Lyon" :

            default:
                break;
        }
    }
}
