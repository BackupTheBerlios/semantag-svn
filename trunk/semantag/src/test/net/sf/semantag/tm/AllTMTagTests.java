package net.sf.semantag.tm;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 * @author cf
 * @version 0.1, created on 13.08.2004
 */
public class AllTMTagTests {
  public static Test suite() {
    TestSuite suite = new TestSuite("Test for org.tm4j.jelly.test.tag.rdf");

    //$JUnit-BEGIN$
    suite.addTestSuite(OpenTopicMapTagTest.class);
    suite.addTestSuite(TopicResolverTest.class);

    //$JUnit-END$
    return suite;
  }
}
