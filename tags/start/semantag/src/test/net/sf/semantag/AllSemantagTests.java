package net.sf.semantag;

import junit.framework.Test;
import junit.framework.TestSuite;

import net.sf.semantag.csv.CSVFileTest;
import net.sf.semantag.rdf.AllRDFTagTests;
import net.sf.semantag.tm.AllTMTagTests;

/**
 *
 * @author cf
 * @version 0.1, created on 13.08.2004
 */
public class AllSemantagTests {
  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.tm4j.jelly.test");

    //$JUnit-BEGIN$
    suite.addTestSuite(CSVFileTest.class);

    //$JUnit-END$
    suite.addTest(AllRDFTagTests.suite());
    suite.addTest(AllTMTagTests.suite());

    return suite;
  }
}
