package net.sf.semantag.tm;

import junit.framework.TestCase;

import net.sf.semantag.TestData;
import net.sf.semantag.tm.OpenTopicMapTag;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.MissingAttributeException;

import org.tm4j.topicmap.TopicMap;

import java.io.File;

/**
 *
 * @author cf
 * @version 0.1, created on 12.08.2004
 */
public class OpenTopicMapTagTest extends TestCase {
  // doTag(XMLOutput) should fail, if no file was set
  public void testValidateFails() throws Exception {
    OpenTopicMapTag tag = new OpenTopicMapTag();

    try {
      tag.doTag(null);
      fail("Expected Missing attribute exception");
    } catch (MissingAttributeException ex) {
      // expected
    }
  }

  /*
   * Class under test for void doTag(XMLOutput)
   */
  public void testOpenLTMTopicMap() throws Exception {
    File f = TestData.getFileFromResource(TestData.TM_JOHN_LTM);
    TopicMap tm = openTMFromPath(f.getAbsolutePath());

    // check that it contains a topic john
    assertNotNull(tm.getTopicByID("john"));
  }

  public void testOpenXTMTopicMap() throws Exception {
    File f = TestData.getFileFromResource(TestData.TM_GREEKS_XTM);
    TopicMap tm = openTMFromPath(f.getAbsolutePath());

    // check that it contains a topic john
    assertNotNull(tm.getTopicByID("Helena"));
  }

  // Helper that performs the test for opening a topicmap
  private TopicMap openTMFromPath(String path) throws Exception {
    // open a ltm-tm from the test resources
    OpenTopicMapTag tag = new OpenTopicMapTag();

    tag.setContext(new JellyContext());

    tag.setFile(path);
    tag.doTag(null);

    // check that the map is accessible
    TopicMap tm = tag.getTopicMap();

    assertNotNull(tm);

    return tm;
  }
}
