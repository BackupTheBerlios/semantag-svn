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
    TestSuite suite = new TestSuite("Test for net.sf.semantag.tm");

    //$JUnit-BEGIN$
    suite.addTestSuite(AddAssociationTagTest.class);
    suite.addTestSuite(AddBasenameTagTest.class);
    suite.addTestSuite(AddInstanceOfTagTest.class);
    suite.addTestSuite(AddMemberTagTest.class);
    suite.addTestSuite(AddOccurrenceTagTest.class);
    suite.addTestSuite(AddSubjectIndicatorTagTest.class);
    suite.addTestSuite(AddTopicTagTest.class);
    suite.addTestSuite(BaseUseTagTest.class);
    suite.addTestSuite(InitTopicMapTagTest.class);
    suite.addTestSuite(OpenTopicMapTagTest.class);
    suite.addTestSuite(TMOResolverTest.class);
    suite.addTestSuite(TopicResolverTest.class);
    suite.addTestSuite(UseAssociationTagTest.class);
    suite.addTestSuite(UseBaseNameTagTest.class);
    suite.addTestSuite(UseMemberTagTest.class);
    suite.addTestSuite(UseOccurrenceTagTest.class);
    suite.addTestSuite(UseTopicTagTest.class);
    
    //$JUnit-END$
    return suite;
  }
}
