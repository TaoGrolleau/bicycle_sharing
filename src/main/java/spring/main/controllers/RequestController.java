package spring.main.controllers;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import spring.main.models.BicycleStation;
import spring.main.repositories.BicycleStationRepository;

import java.util.List;
import java.util.Optional;

@Controller
@ComponentScan
public class RequestController {
    @Autowired
    BicycleStationRepository bicycleStationRepository;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/city")
    public String request_city(Model model, @RequestParam("name") String city_name) {
        bicycleStationRepository.deleteAll();
        BicycleStation station;
        RDFConnection conn = RDFConnectionFactory.connect("http://localhost:3030/bicycle-sharing/query", null, null);
        QueryExecution qExec = conn.query("PREFIX ex: <http://example.org/>\n" +
                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX vocab: <http://rdf.ontology2.com/vocab#>\n" +
                "PREFIX geo:   <http://www.opengis.net/ont/geosparql#>\n" +
                "PREFIX dbo:   <http://dbpedia.org/ontology/>" +
                "SELECT ?subject ?label ?id ?lat ?lon ?cap ?avBikes ?avStands\n" +
                "WHERE {\n" +
                "   ?subject rdfs:label ?label .\n" +
                "   ?subject dbo:idNumber ?id .\n" +
                "   ?subject geo:lat ?lat .\n" +
                "   ?subject geo:lon ?lon .\n" +
                "   ?subject geo:location \"" + city_name + "\" .\n" +
                "   ?subject vocab:capacity ?cap .\n" +
                "   ?subject ex:availableBikes ?avBikes .\n" +
                "   ?subject ex:availableStands ?avStands .\n" +
                "}\n");
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            Literal label = qs.getLiteral("?label");
            Literal id = qs.getLiteral("?id");
            Literal lon = qs.getLiteral("?lon");
            Literal lat = qs.getLiteral("?lat");
            Literal cap = qs.getLiteral("?cap");
            Literal avBikes = qs.getLiteral("?avBikes");
            Literal avStands = qs.getLiteral("?avStands");

            station = new BicycleStation(label.toString(), id.toString(), lon.toString(), lat.toString(), avStands.toString(), avBikes.toString(), cap.toString(), city_name);
            System.out.println(station);
            bicycleStationRepository.save(station);
        }

        qExec.close();
        conn.close();

        model.addAttribute("stations", bicycleStationRepository.findAll());
        //model.addAttribute("stations", bicycleStationRepository.findByCity(city_name));

        return "city";
    }

    @RequestMapping("/station")
    public String request_station(Model model, @RequestParam("id") String id) {
        Optional<BicycleStation> result = bicycleStationRepository.findById(Long.parseLong(id));
        if (result.isPresent()) {
            model.addAttribute("station_found", result.get());
        }

        return "station";
    }

    @RequestMapping("/problem")
    public String request_error(){
        return "problem";
    }
}
