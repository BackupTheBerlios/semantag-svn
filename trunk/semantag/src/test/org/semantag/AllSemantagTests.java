package org.semantag;

import org.semantag.csv.CSVFileTest;
import org.semantag.rdf.AllRDFTagTests;
import org.semantag.tm.AllTMTagTests;

import junit.framework.Test;
import junit.framework.TestSuite;


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
