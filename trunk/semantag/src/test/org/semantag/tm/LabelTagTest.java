package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.semantag.TestData;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 29.11.2004
 */
public class LabelTagTest extends TMTagTestBase {

    TopicMap tm;

    LabelTag labelTag;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM,
                TestData.TM_JOHN_BASELOCATOR);

        labelTag = new LabelTag();

    }

    // getter/setter test
    public void testSetTmo() {

        // initially no tmo should be set
        assertNull(labelTag.getTmo());

        // set a topic map object and assert that it
        // can be retrieved
        Topic john = tm.getTopicByID("john");
        labelTag.setTmo(john);
        assertEquals(john, labelTag.getTmo());
    }

    /*
     * test the label printing
     */
    public void testDoTagXMLOutput() {
        
    }

    
    
    // test the validation - method
    public void testValidate() throws Exception {

        // validating an instance that has
        // no topic map object set shall lead to an exception
        assertNull(labelTag.getTmo());
        try {
            labelTag.validate();
            
            String er = "Expected an exception when validating an instance ";
            er +="of LabelTag with no topicmap object set.";
            fail(er);
        } catch (JellyTagException e) {
           
        }

        // validating an instance that has
        // a topic map object set should pass
        labelTag.setTmo(tm.getTopicByID("john"));
        labelTag.validate();
        
        
    }
}