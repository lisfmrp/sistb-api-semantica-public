package br.usp.websemantica.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringWriter;

import org.apache.any23.Any23;
import org.apache.any23.filter.IgnoreAccidentalRDFa;
import org.apache.any23.filter.IgnoreTitlesOfEmptyDocuments;
import org.apache.any23.http.HTTPClient;
import org.apache.any23.source.DocumentSource;
import org.apache.any23.source.HTTPDocumentSource;
import org.apache.any23.writer.ReportingTripleHandler;
import org.apache.any23.writer.TripleHandler;
import org.apache.any23.writer.TurtleWriter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.Logger;

public class Utils {

	static final Logger log = Logger.getLogger(Utils.class);

	public static boolean isValidFormat(String format) {
		if (format.equals("rdf") || format.equals("xml") || format.equals("ntriples") || format.equals("n3") || format.equals("turtle")
				 || format.equals("ttl") || format.equals("rdf+xml") || format.equals("json-ld")) {
			return true;
		}

		return false;
	}
	
	public static String extractPageData(String url, String format) {
		try {
			Any23 runner = new Any23();
			runner.setHTTPUserAgent("test-user-agent");

			HTTPClient httpClient = runner.getHTTPClient();
			DocumentSource source = new HTTPDocumentSource(httpClient, url.trim());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			TripleHandler handler = new TurtleWriter(out);

			runner.extract(source, handler);
			handler.close();

			InputStream in = new ByteArrayInputStream(out.toByteArray());
			Model model = ModelFactory.createDefaultModel();
			model.setNsPrefix("sistb", "http://sistb-dev.ddns.net");

			model.read(in, null, "Turtle");
			StringWriter sw = new StringWriter();

			if (format.equals("xml") || format.equals("rdf")) {
				model.write(sw, "RDF/XML-ABBREV");
			} else if (format.equals("ntriples") || format.equals("n3")) {
				model.write(sw, "NTRIPLES");
			} else if (format.equals("turtle") || format.equals("ttl")) {
				model.write(sw, "Turtle");
			}  else if (format.equals("json-ld")) {
				model.write(sw, "JSON-LD");
			}

			return sw.toString();

		} catch (Exception e) {
			log.error("Error extracting data from page " + url);
			// e.printStackTrace();
		}
		return null;
	}

	public static Model extractPageDataAsModel(String url) {
		try {
			Any23 runner = new Any23();
			runner.setHTTPUserAgent("test-user-agent");		
			
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			TripleHandler handler = new org.apache.any23.writer.RDFXMLWriter(out);
			
			TripleHandler tripleHandler = new ReportingTripleHandler(
			        new IgnoreAccidentalRDFa(
			                new IgnoreTitlesOfEmptyDocuments(handler),
			                true // if true the CSS triples will be removed in any case.
			        )
			);
						
			HTTPClient httpClient = runner.getHTTPClient();
			DocumentSource source = new HTTPDocumentSource(httpClient, url.trim());				
			runner.extract(source, tripleHandler);		
			handler.close();
			tripleHandler.close();
					
			//System.out.println(out.toString("UTF-8"));
			InputStream in = new ByteArrayInputStream(out.toByteArray());
			
			Model model = ModelFactory.createDefaultModel();
    		model.setNsPrefix("sistb", "http://tuberculosis.cloud");
    		
			model.read(in, null, "RDF/XML-ABBREV");
			return model;
		} catch (Exception e) {
			log.error("Error extracting data from page");
			e.printStackTrace();
		}
		return null;	
	}
	
	public static String convertModelFormat(Model model, String format) {
		StringWriter sw = new StringWriter();

		if (format.equals("xml") || format.equals("rdf")) {
			model.write(sw, "RDF/XML-ABBREV");
		} else if (format.equals("ntriples") || format.equals("n3")) {
			model.write(sw, "NTRIPLES");
		} else if (format.equals("turtle") || format.equals("ttl")) {
			model.write(sw, "Turtle");
		}  else if (format.equals("json-ld")) {
			model.write(sw, "JSON-LD");
		}

		return sw.toString();
	}
}
