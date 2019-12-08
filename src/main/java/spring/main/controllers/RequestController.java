package spring.main.controllers;

import org.apache.jena.graph.impl.LiteralLabel;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import spring.main.models.BicycleStation;
import spring.main.repositories.BicycleStationRepository;

import java.util.List;

@Controller
@ComponentScan
public class RequestController {
    @Autowired
    BicycleStationRepository bicycleStationRepository;

    @RequestMapping("/city")
    public String request_city(Model model) {
        bicycleStationRepository.deleteAll();
        BicycleStation station;
        String city = "Strasbourg";
        RDFConnection conn = RDFConnectionFactory.connect("http://localhost:3030/bicycle-sharing/query", null, null);
        QueryExecution qExec = conn.query("PREFIX ex: <http://example.com/>\n" +
                "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX vocab: <http://rdf.ontology2.com/vocab#>\n" +
                "PREFIX geo:   <http://www.opengis.net/ont/geosparql#>\n" +
                "PREFIX dbo:   <http://dbpedia.org/ontology/>" +
                "SELECT ?subject ?label ?id ?avBikes ?avStands ?cap ?lat ?lon\n" +
                "WHERE {\n" +
                "   ?subject rdfs:label ?label .\n" +
                "   ?subject dbo:idNumber ?id .\n" +
                "   ?subject ex:availableBikes ?avBikes .\n" +
                "   ?subject ex:availableStands ?avStands .\n" +
                "   ?subject vocab:capacity ?cap .\n" +
                "   ?subject geo:lat ?lat .\n" +
                "   ?subject geo:lon ?lon .\n" +
                "   ?subject geo:location \""+city+"\" .\n" +
                "}\n");
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            Literal label = qs.getLiteral("?label");
            Literal id = qs.getLiteral("?id");
            Literal lon = qs.getLiteral("?lon");
            Literal lat = qs.getLiteral("?lat");
            Literal avBikes = qs.getLiteral("?avBikes");
            Literal avStands = qs.getLiteral("?avStands");
            Literal cap = qs.getLiteral("?cap");

            station = new BicycleStation(label.toString(), id.toString(), lon.toString(), lat.toString(), avBikes.toString(), avStands.toString(), cap.toString());
            bicycleStationRepository.save(station);
        }
        model.addAttribute("stations", bicycleStationRepository.findAll());
        qExec.close();
        conn.close();

        return "city";
    }
}
