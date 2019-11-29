package spring.main.controllers;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RequestController {
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
}
