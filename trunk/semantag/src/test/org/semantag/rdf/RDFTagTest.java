package org.semantag.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;

import junit.framework.TestCase;


import java.net.URL;

import org.semantag.TestData;
import org.semantag.rdf.RDFTag;

/**
 *
 * @author cf
 * @version 0.1, created on 11.08.2004
 */
public class RDFTagTest extends TestCase {
  public void testMakeAndFillModel() throws Exception {
    RDFTag rdftag = new RDFTag();
    URL url = TestData.getURLFromResource(TestData.RDF_HAMAIRPORT);

    rdftag.setUrl(url.toExternalForm());
    rdftag.setModelname("rdfModelTest");
    rdftag.setModelroot("/tmp/");

    Model model = rdftag.makeAndFillModel();

    assertNotNull(model);

    Property longitude = model.getProperty("http://www.w3.org/2003/01/geo/wgs84_pos",
                                           "#long");
    NodeIterator it = model.listObjectsOfProperty(longitude);

    assertTrue(it.hasNext());
    assertEquals("9.9833333333333", it.nextNode().toString());
    assertFalse(it.hasNext()); // there was only one
  }
}

//<?xml version="1.0" encoding="UTF-8"?><rdf:RDF
//        xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
//        xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
//        xmlns:airport="http://www.daml.org/2001/10/html/airport-ont#"
//        xmlns:geo="http://www.w3.org/2003/01/geo/wgs84_pos#">
//        <airport:Airport rdf:about="http://www.daml.org/cgi-bin/airport?HAM">
//            <airport:iataCode>HAM</airport:iataCode>
//            <geo:lat>53.633333333333</geo:lat>
//            <geo:long>9.9833333333333</geo:long>
//            <rdfs:seeAlso rdf:resource="http://www.daml.org/cgi-bin/airport?HAM"/>
//        </airport:Airport>
//        </rdf:RDF>
