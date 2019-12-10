package spring.main.controllers;

import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateRequest;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import spring.main.models.BicycleStation;
import spring.main.repositories.BicycleStationRepository;

import javax.xml.bind.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

@Controller
public class MainController {
    @Autowired
    BicycleStationRepository bicycleStationRepository;

    @RequestMapping(value="/")
    //Fonction d'initialisation
    public String index() throws IOException, ParseException {
        RDFConnection conn = RDFConnectionFactory.connect("http://localhost:3030/bicycle-sharing/update");

        UpdateRequest clear = UpdateFactory.create("CLEAR DEFAULT");
        UpdateRequest update = UpdateFactory.create("" +
                "BASE <http://example.org/>\n" +
                "PREFIX geo: <http://www.opengis.net/ont/geosparql#>\n" +
                "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX ex: <http://example.org/>\n" +
                "PREFIX vocab: <http://rdf.ontology2.com/vocab#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX ite: <http://w3id.org/sparql-generate/iter/>\n" +
                "PREFIX crm: <http://www.cidoc-crm.org/cidoc-crm/>");

        bicycleStationRepository.deleteAll();

        //get_data("Rennes");
        get_data("Paris");
        get_data("Lyon");
        get_data("Saint-Etienne");
        get_data_XML("Montpellier");
        get_data_XML("Strasbourg");

        return "index";
    }

    public void get_data(String city, UpdateRequest update) throws IOException, ParseException {
        String inline = "";
        URL url;
        switch (city) {
            case "Rennes":
                url = new URL("https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=etat-des-stations-le-velo-star-en-temps-reel");
                break;
            case "Lyon":
                url = new URL("https://download.data.grandlyon.com/wfs/rdata?SERVICE=WFS&VERSION=1.1.0&outputformat=GEOJSON&request=GetFeature&typename=jcd_jcdecaux.jcdvelov&SRSNAME=urn:ogc:def:crs:EPSG::4171");
                break;
            case "Paris":
                url = new URL("https://opendata.paris.fr/api/records/1.0/search/?dataset=velib-disponibilite-en-temps-reel&facet=overflowactivation&facet=creditcard&facet=kioskstate&facet=station_state");
                break;
            case "Saint-Etienne" :
                url = new URL("https://saint-etienne-gbfs.klervi.net/gbfs/en/station_information.json");
                break;
            default:
                url = new URL("https://data.rennesmetropole.fr/api/records/1.0/search/?dataset=etat-des-stations-le-velo-star-en-temps-reel");
                break;
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

            sc.close();
        }

        JSONParser jsonParser = new JSONParser();
        JSONObject jobj = (JSONObject) jsonParser.parse(inline);

        switch (city) {
            case "Rennes":
                JSONArray array_rennes = (JSONArray) jobj.get("records");

                for (int i = 0; i < array_rennes.size(); i++) {
                    JSONObject json_obj_1 = (JSONObject) array_rennes.get(i);
                    JSONObject fields = (JSONObject) json_obj_1.get("fields");
                    JSONArray coordonnees = (JSONArray) fields.get("coordonnees");

                    String query = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> a ex:station .\n" +
                            "}";
                    String query2 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> dbo:idNumber "+fields.get("idstation").toString()+".\n" +
                            "}";
                    String query3 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> rdfs:label \""+ fields.get("nom").toString() +"\" .\n" +
                            "}";
                    String query4 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> geo:lat "+coordonnees.get(0).toString()+".\n" +
                            "}";
                    String query5 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> geo:lon "+coordonnees.get(1).toString()+".\n" +
                            "}";
                    String query6 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> vocab:capacity "+fields.get("nombrevelosdisponibles").toString()+".\n" +
                            "}";
                    String query7 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> ex:availableStands "+fields.get("nombreemplacementsdisponibles").toString()+".\n" +
                            "}";
                    String query8 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> ex:availableBikes "+fields.get("nombreemplacementsactuels").toString()+".\n" +
                            "}";
                    String query9 = "INSERT DATA {\n" +
                            "<http://ex.com/" + fields.get("idstation").toString() +"> geo:location \"Rennes\" ." +
                            "}";
                    update.add(query);
                    update.add(query2);
                    update.add(query3);
                    update.add(query4);
                    update.add(query5);
                    update.add(query6);
                    update.add(query7);
                    update.add(query8);
                    update.add(query9);

                    /*BicycleStation bicycleStationToAdd = new BicycleStation(
                            fields.get("nom").toString(),
                            fields.get("idstation").toString(),
                            coordonnees.get(1).toString(),
                            coordonnees.get(0).toString(),
                            fields.get("nombrevelosdisponibles").toString(),
                            fields.get("nombreemplacementsdisponibles").toString(),
                            fields.get("nombreemplacementsactuels").toString(),
                            city
                    );
                    bicycleStationRepository.save(bicycleStationToAdd);
                }
                break;
            case "Lyon":
                JSONArray array_lyon = (JSONArray) jobj.get("features");

                for (int i = 0; i < array_lyon.size(); i++) {
                    JSONObject json_obj_lyon = (JSONObject) array_lyon.get(i);
                    JSONObject geometry = (JSONObject) json_obj_lyon.get("geometry");
                    JSONObject properties = (JSONObject) json_obj_lyon.get("properties");
                    JSONArray coordinates = (JSONArray) geometry.get("coordinates");

                    String query = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> a ex:station\n" +
                            "}";
                    String query2 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> dbo:idNumber "+properties.get("number").toString()+" .\n" +
                            "}";
                    String query3 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> rdfs:label \""+ properties.get("name").toString() +"\" .\n" +
                            "}";
                    String query4 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> geo:lat "+coordinates.get(1).toString()+" .\n" +
                            "}";
                    String query5 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> geo:lon "+coordinates.get(0).toString()+" .\n" +
                            "}";
                    String query6 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> vocab:capacity \""+properties.get("bike_stands").toString()+"\" .\n" +
                            "}";
                    String query7 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> ex:availableStands \""+properties.get("available_bike_stands").toString()+"\" .\n" +
                            "}";
                    String query8 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> ex:availableBikes \""+properties.get("available_bikes").toString()+"\" .\n" +
                            "}";
                    String query9 = "INSERT DATA {\n" +
                            "<http://ex.com/" + properties.get("number").toString() +"> geo:location \"Lyon\" ." +
                            "}";
                    System.out.println(query5+"\n"+query6);
                    update.add(query);
                    update.add(query2);
                    update.add(query3);
                    update.add(query4);
                    update.add(query5);
                    update.add(query6);
                    update.add(query7);
                    update.add(query8);
                    update.add(query9);

                    /*BicycleStation bicycleStationToAdd = new BicycleStation(
                            properties.get("name").toString(),
                            properties.get("number").toString(),
                            coordinates.get(0).toString(),
                            coordinates.get(1).toString(),
                            properties.get("available_bikes").toString(),
                            properties.get("available_bike_stands").toString(),
                            properties.get("bike_stands").toString(),
                            city
                    );
                    bicycleStationRepository.save(bicycleStationToAdd);*/
                }
                break;
            case "Paris":
                JSONArray array_paris = (JSONArray) jobj.get("records");

                for (int i = 0; i < array_paris.size(); i++){
                    JSONObject json_obj_paris = (JSONObject) array_paris.get(i);
                    JSONObject geometry = (JSONObject) json_obj_paris.get("geometry");
                    JSONObject fields = (JSONObject) json_obj_paris.get("fields");
                    JSONArray coordinates = (JSONArray) geometry.get("coordinates");

                    BicycleStation bicycleStationToAdd = new BicycleStation(
                            fields.get("station_name").toString(),
                            fields.get("station_code").toString(),
                            coordinates.get(0).toString(),
                            coordinates.get(1).toString(),
                            fields.get("nbebike").toString(),
                            fields.get("nbfreeedock").toString(),
                            fields.get("nbedock").toString(),
                            city
                    );
                    bicycleStationRepository.save(bicycleStationToAdd);
                }
                break;
            case "Saint-Etienne":
                String inline2 = "";
                URL url2 = new URL("https://saint-etienne-gbfs.klervi.net/gbfs/en/station_status.json");
                HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
                conn2.setRequestMethod("GET");
                conn2.connect();
                int responsecode2 = conn2.getResponseCode();

                if (responsecode2 != 200) {
                    throw new RuntimeException("HttpResponseCode: " + responsecode2);
                } else {
                    Scanner sc2 = new Scanner(url2.openStream());

                    while (sc2.hasNext()) {
                        inline2 += sc2.nextLine();
                    }
                    System.out.println("\nJSON data in string format");
                    System.out.println(inline2);

                    sc2.close();
                }

                JSONParser jsonParser2 = new JSONParser();
                JSONObject jobj2 = (JSONObject) jsonParser2.parse(inline2);

                JSONObject data_ste = (JSONObject) jobj.get("data");
                JSONObject data_ste_status = (JSONObject) jobj2.get("data");

                System.out.println(data_ste);

                JSONArray array_ste = (JSONArray) data_ste.get("stations");
                JSONArray array_ste_status = (JSONArray) data_ste_status.get("stations");

                for(int i = 0; i < array_ste.size(); i++){
                    JSONObject json_obj_ste = (JSONObject) array_ste.get(i);
                    JSONObject json_obj_ste_status = (JSONObject) array_ste_status.get(i);

                    System.out.println();

                    BicycleStation bicycleStationToAdd = new BicycleStation(
                            json_obj_ste.get("name").toString(),
                            json_obj_ste.get("station_id").toString(),
                            json_obj_ste.get("lon").toString(),
                            json_obj_ste.get("lat").toString(),
                            json_obj_ste_status.get("num_bikes_available").toString(),
                            json_obj_ste_status.get("num_docks_available").toString(),
                            json_obj_ste.get("capacity").toString(),
                            city
                    );
                    bicycleStationRepository.save(bicycleStationToAdd);


                }
            default:
                break;
        }
    }

    public void get_data_XML(String city) throws IOException {
        URL url;

        switch (city){
            case "Montpellier":
                url = new URL("https://data.montpellier3m.fr/sites/default/files/ressources/TAM_MMM_VELOMAG.xml");
                break;
            case "Strasbourg":
                url = new URL("http://velhop.strasbourg.eu/tvcstations.xml");
                break;
            default:
                url = new URL("http://velhop.strasbourg.eu/tvcstations.xml");
                break;
        }

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(url.openStream());

            NodeList nodes = doc.getElementsByTagName("si");

            for(int i=0; i<nodes.getLength();i++) {
                BicycleStation bicycleStationToAdd = new BicycleStation(
                        nodes.item(i).getAttributes().getNamedItem("na").getNodeValue(),
                        nodes.item(i).getAttributes().getNamedItem("id").getNodeValue(),
                        nodes.item(i).getAttributes().getNamedItem("lg").getNodeValue(),
                        nodes.item(i).getAttributes().getNamedItem("la").getNodeValue(),
                        nodes.item(i).getAttributes().getNamedItem("av").getNodeValue(),
                        nodes.item(i).getAttributes().getNamedItem("fr").getNodeValue(),
                        nodes.item(i).getAttributes().getNamedItem("to").getNodeValue(),
                        city
                );
                bicycleStationRepository.save(bicycleStationToAdd);
            }



        } catch (ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }



    }
}
