package net.sf.semantag.tm;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Tests for class BaseUseTag
 * 
 * @author cf
 * @version 0.1, created on 09.09.2004
 */
public class BaseUseTagTest extends TMTagTestBase {

    BaseUseTag but ;
    
    protected void setUp() throws Exception {
        
        // make a non abstract instance
        but = new BaseUseTag(){
            public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {
            }

            protected TopicMapObject createTMO() throws JellyTagException {
                return null;
            }    
            
               
        };
    }
    
    // checks proper storage of attributes
    // that are located in class BaseUseTag
    public void testStoreProperties() 
    throws Exception{
       
        String idValue = "v";
        String slValue = "sl";
        TopicMap tm = getTopicMapFromResource(TestData.TM_JOHN_LTM,
                TestData.TM_JOHN_BASELOCATOR);
        
        assertNull(but.getId());
        assertNull(but.getSourceLocator());
        assertNull(but.getTopicmap());
        
        but.setId(idValue);
        but.setSourceLocator(slValue);
        but.setTopicmap(tm);
        
        assertEquals(idValue, but.getId());
        assertEquals(slValue, but.getSourceLocator());
        assertEquals(tm, but.getTopicmap());
    }


    public void testNonexistantModes() {
        String mode;
        
        // the tag uses initially the default mode
        assertEquals(but.getNonexistant(), BaseUseTag.NE_DEFAULT);
        
        // check FAIL, upper and lower case
        mode = BaseUseTag.NE_FAIL;
        checkNonExistantModes(mode.toLowerCase(), mode, true, false, false);
        checkNonExistantModes(mode.toUpperCase(), mode, true, false, false);
        
        // check ADD, upper and lower case
        mode = BaseUseTag.NE_ADD;
        checkNonExistantModes(mode.toLowerCase(), mode, false, true, false);
        checkNonExistantModes(mode.toUpperCase(), mode, false, true, false);
        
        // check IGNORE, upper and lower case
        mode = BaseUseTag.NE_IGNORE_BODY;
        checkNonExistantModes(mode.toLowerCase(), mode, false, false, true);
        checkNonExistantModes(mode.toUpperCase(), mode, false, false, true);
        
        // check that unknown mode causes the
        // default to be used
        // default is assumed to be IGNORE
        mode = "foo";
        checkNonExistantModes(mode.toLowerCase(), BaseUseTag.NE_IGNORE_BODY, false, false, true);

        // check that null mode causes the
        // default to be used
        // default is assumed to be IGNORE
        mode = null;
        checkNonExistantModes(mode, BaseUseTag.NE_IGNORE_BODY, false, false, true);

        
    }
    /**
     * Checks that setting the nonexistant - property of the
     * BaseUseTag to <code>mode</code> leads to the following
     * results.
     * <ul>
     *  <li>shallFailOnNonexistant() equals to <code>fail</code></li>
     *  <li>shallAddOnNonexistant() equals to <code>add</code></li>
     *  <li>shallIgnoreOnNonexistant() equals to <code>ignore</code></li>
     *  <li>getNonexistant() equals to <code>storedMode</code></li>
     * </ul>
     * @param mode the mode to pass in BaseUseTag
     * @param storedMode the mode that BaseUseTag returns
     * @param fail the expected result of shallFailOnNonexistant
     * @param add the expected result of shallAddOnNonexistant
     * @param ignore the expected result of shallIgnoreOnNonexistant
     */
    private void checkNonExistantModes(String mode, String storedMode,
            boolean fail, boolean add, boolean ignore){
       
        
        // set mode 
        but.setNonexistant(mode);
        
        //check if the shortcut methods behave as expected
        assertEquals(fail, but.shallFailOnNonexistant());
        assertEquals(add, but.shallAddOnNonexistant());
        assertEquals(ignore, but.shallIgnoreOnNonexistant());

        // check if the string that is stored for the mode
        // is as expected
        assertEquals(storedMode, but.getNonexistant());
        
    }
    
}
