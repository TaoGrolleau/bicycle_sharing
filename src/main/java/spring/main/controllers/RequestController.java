package spring.main.controllers;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
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

@Controller
@ComponentScan
public class RequestController {
    @Autowired
    BicycleStationRepository bicycleStationRepository;

    @RequestMapping("/request")
    public String request() {
        String answer = "";
        RDFConnection conn = RDFConnectionFactory.connect("http://localhost:3030/zimmermann/query", null, null);
        QueryExecution qExec = conn.query("SELECT ?subject ?predicate ?object\n" +
                "WHERE {\n" +
                "  ?subject a ?object\n" +
                "}\n" +
                "LIMIT 25");
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            Resource subject = qs.getResource("subject");
            answer += subject;
        }

        qExec.close();
        conn.close();
        //return chemin de la page HTML
        return answer;
    }

    @RequestMapping("/city")
    public String request_city(Model model) {
        BicycleStation station;
        RDFConnection conn = RDFConnectionFactory.connect("http://localhost:3030/bicycle-sharing/query", null, null);
        QueryExecution qExec = conn.query("PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "SELECT ?subject ?predicate ?object\n" +
                "WHERE {\n" +
                "  ?subject dbo:idNumber ?object\n" +
                "}\n" +
                "LIMIT 25");
        ResultSet rs = qExec.execSelect();
        while (rs.hasNext()) {
            QuerySolution qs = rs.next();
            Literal r = qs.getLiteral("object");
            station = new BicycleStation("Nom", r.toString(), "lon", "lat", "Available Bikes", "Used Bikes", "Total Bikes");
            bicycleStationRepository.save(station);
        }
        model.addAttribute("stations", bicycleStationRepository.findAll());
        qExec.close();
        conn.close();

        return "city";
    }
}
