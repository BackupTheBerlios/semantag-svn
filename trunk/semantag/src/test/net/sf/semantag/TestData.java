package net.sf.semantag;

import java.io.File;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 *
 * @author cf
 * @version 0.1, created on 19.05.2004
 */
public class TestData {
  // RESOURCE_BASE
  private final static String RB = "/net/sf/semantag/resource/";
  public final static String CSVTEST = RB + "csvTest.csv";
  public final static String ADVANCEDCSVTEST = RB + "csvTest2.csv";
  public final static String TM_JOHN_LTM = RB + "about_john.ltm";
  public final static String TM_GREEKS_XTM = RB + "greek.xtm.xml";
  public final static String TM_TEST2_XTM = RB + "test2.xtm.xml";
  public final static String RDF_HAMAIRPORT = RB + "rfkrlutf.rdf";

  public final static String TM_JOHN_BASELOCATOR = "about_john";
  public static final String TM_GREEKS_BASELOCATOR = "http://greeksxtm.org";
  
  public static File getFileFromResource(String resource)
                                  throws URISyntaxException {
    return new File(new URI(TestData.class.getResource(resource).toExternalForm()));
  }

  public static URL getURLFromResource(String resource)
                                throws URISyntaxException {
    return TestData.class.getResource(resource);
  }
}
