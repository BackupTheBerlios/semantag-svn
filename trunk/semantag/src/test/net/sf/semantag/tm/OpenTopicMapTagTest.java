package net.sf.semantag.tm;

import java.io.File;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.MissingAttributeException;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 12.08.2004
 */
public class OpenTopicMapTagTest extends BaseTMTagTest {
   
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

    // opens a ltm notated topic map
    public void testOpenLTMTopicMap() throws Exception {
        File f = TestData.getFileFromResource(TestData.TM_JOHN_LTM);
        TopicMap tm = openTMFromPath(f.getAbsolutePath());

        // check that it contains a topic john
        assertNotNull(tm.getTopicByID("john"));
    }

    //  opens a topicmap in xtm notation
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
        setScriptForTagBody(tag);
        
        tag.setFile(path);
        tag.doTag(null);

        // check that the map is accessible
        TopicMap tm = tag.getTopicMapFromContext(null);
        assertNotNull(tm);

        //check that it is accessible via getTopicMap() too
        assertEquals(tm, tag.getTopicMap());
        
        return tm;
    }
}