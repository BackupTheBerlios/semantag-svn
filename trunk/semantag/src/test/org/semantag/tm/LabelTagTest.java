package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;
import org.semantag.TestData;
import org.tm4j.net.Locator;
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

    // String that is used by the XMLOutput-Mock
    // to store the data that was written
    String writtenToOutputMock;
    
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
        assertNull(labelTag.getObject());

        // set a topic map object and assert that it
        // can be retrieved
        Topic john = tm.getTopicByID("john");
        labelTag.setObject(john);
        assertEquals(john, labelTag.getObject());
    }

    /*
     * test the label printing
     */
    public void testDoTagXMLOutput() throws Exception{
        
        Topic john = tm.getTopicByID("john");
        labelTag.setObject(john);
        labelTag.doTag(getOutputMock());
        assertEquals("John", writtenToOutputMock);
        
        String sand =" Sand";
        labelTag.setObject(sand);
        labelTag.doTag(getOutputMock());
        assertEquals(sand, writtenToOutputMock);

        Locator l = (Locator)john.getSourceLocators().iterator().next();
        labelTag.setObject(l);
        labelTag.doTag(getOutputMock());
        assertEquals(l.getAddress(), writtenToOutputMock);

    }
    

    
    
    // test the validation - method
    public void testValidate() throws Exception {

        // validating an instance that has
        // no topic map object set shall lead to an exception
        assertNull(labelTag.getObject());
        try {
            labelTag.validate();
            
            String er = "Expected an exception when validating an instance ";
            er +="of LabelTag with no topicmap object set.";
            fail(er);
        } catch (JellyTagException e) {
           
        }

        // validate that an instance that has
        // a topic map object set should pass
        labelTag.setObject(tm.getTopicByID("john"));
        labelTag.validate();
        
        
        // validate that getting a label 
        // for a locator succeeds
        Topic john = tm.getTopicByID("john");
        Locator l = (Locator)john.getSourceLocators().iterator().next();
        labelTag.setObject(l);
        labelTag.validate();
        
        // validate that getting a label for an
        // arbitrary object succeeds
        labelTag.setObject("hi");
        labelTag.validate();
        
        
    }
    
    
    
    private XMLOutput getOutputMock(){
        
        return new XMLOutput(){
            
            public void write(String arg){
                writtenToOutputMock = arg;
            }
            
           
        };
    }
}