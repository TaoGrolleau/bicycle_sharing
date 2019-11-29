import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.*;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.rdfconnection.SparqlQueryConnection;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import java.io.*;

public class FirstJenaProject {
    static public void jena_project (String[] args) throws FileNotFoundException {

        String personURI = "http://somewhere/JohnSmith";
        String fullName = "John Smith";
        String name = "Smith";
        String foafNS = "http://xmlns.com/foaf/0.1/";
        String age = "23";

        Model m = ModelFactory.createDefaultModel();
        Resource johnSmith = m.createResource(personURI);


        Property foafname = m.createProperty(foafNS + "name");
        Property foafage = m.createProperty(foafNS + "age");
        johnSmith.addProperty(foafage, "38", XSDDatatype.XSDinteger);
        johnSmith.addProperty(foafname, name);
        johnSmith.addProperty(RDFS.label, fullName, "en");

        SparqlQueryConnection conn = RDFConnectionFactory.connect("http://dbpedia.org/sparql", null, null);
        QueryExecution qe = conn.query("SELECT * WHERE { ?s ?p ?o } LIMIT 10)");
        ResultSet rs = qe.execSelect();
        while(rs.hasNext()) {
            QuerySolution qs = rs.next();
            RDFNode s = qs.get("S");
            RDFNode p = qs.get("P");
            RDFNode o = qs.get("O");
            System.out.println("subject: " + s + ", predicate: " + p + ", object: " + o);
        }

        //m.read("http://dbpedia.org/resource/Saint-Étienne");
        m.write(System.out, "Turtle"); //Autres formats : Turtle, N-TRIPLES

        //******************************

        String exURI = "http://www.example.com/";
        String geoURI = "http://www.w3.org/2003/01/geo/wgs84_pos#";
        String rdfsURI = "http://www.w3.org/2000/01/rdf-schema#";

        Model m_stations = ModelFactory.createDefaultModel();

        Resource geoThing = m_stations.createResource(geoURI + "SpatialThing");

        Property lat = m_stations.createProperty(geoURI + "lat");
        Property lon = m_stations.createProperty(geoURI + "lon");

        m_stations.setNsPrefix("ex", exURI);
        m_stations.setNsPrefix("rdfs", rdfsURI);
        m_stations.setNsPrefix("geo", geoURI);

        /* File file = new File("C:\\Users\\taogr\\IdeaProjects\\bicycle_sharing\\src\\main\\resources\\stops.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        String[] splittedLine;

        StationModel sm = new StationModel();

        try {
            line = br.readLine(); //Remove first line
            while ((line = br.readLine()) != null) {
                // process the line.
                splittedLine = line.split(",");

                String stop_id = splittedLine[0].replace(" ", ":");
                sm.setStopID(stop_id);


                String stop_name = splittedLine[1].replace("\"", "");
                sm.setStopName(stop_name);
                sm.setStopLat(splittedLine[3]);
                sm.setStopLon(splittedLine[4]);

                Resource station1 = m_stations.createResource(exURI + sm.getStopID());
                station1.addProperty(RDF.type, geoThing);
                station1.addProperty(RDFS.label, sm.getStopName(), "fr");
                station1.addProperty(lat, sm.getStopLat(), XSDDatatype.XSDdecimal);
                station1.addProperty(lon, sm.getStopLon(), XSDDatatype.XSDdecimal);

                //sm.printSM();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        m_stations.write(System.out, "Turtle");
         */
    }
}
