package org.semantag.tm;

import java.io.File;


import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.MissingAttributeException;
import org.semantag.TestData;
import org.semantag.tm.OpenTopicMapTag;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 12.08.2004
 */
public class OpenTopicMapTagTest extends TMTagTestBase {
   
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
        TopicMap tm = openTMFromPath(f.getAbsolutePath(), new OpenTopicMapTag());

        // check that it contains a topic john
        assertNotNull(tm.getTopicByID("john"));
    }

    //  opens a topicmap in xtm notation
    public void testOpenXTMTopicMap() throws Exception {
        File f = TestData.getFileFromResource(TestData.TM_GREEKS_XTM);
        TopicMap tm = openTMFromPath(f.getAbsolutePath(), new OpenTopicMapTag());

        // check that it contains a topic john
        assertNotNull(tm.getTopicByID("Helena"));
    }

    
    // tests that specifying a variable leads to
    // the topicmap being accessible through this
    // variable
    public void testTMStoredInVariable()
        throws Exception
    {
        File f = TestData.getFileFromResource(TestData.TM_GREEKS_XTM);
        
        OpenTopicMapTag otmt = new OpenTopicMapTag();
        otmt.setVar("TOPICMAP");
        
        TopicMap tm = openTMFromPath(f.getAbsolutePath(), otmt);

        // assert that context contains the
        // topicmap under the name expected
        assertEquals(tm, otmt.getContext().getVariable("TOPICMAP"));
        
    }

    
    // Helper that performs the test for opening a topicmap
    private TopicMap openTMFromPath(String path, OpenTopicMapTag tag) throws Exception {
        
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