package net.sf.semantag.tm;

import java.util.Iterator;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class AddSubjectIndicatorTagTest extends TMTagTestBase {

    private TopicMap tm;

    private AddSubjectIndicatorTag asit;

    private JellyContext ctx;

    // an often used topic
    private Topic john;

    // an existing id 
    final String personID  = "tt-person";
    
    // used to generate unique ids for every  
    // basename added.
    private static int addCount = 0;
    
    // test adding a subject indicator to a topic that is 
    // defined by a parent TopicTag
    public void testAddSubjectIndicatorToExplicitTopic() throws Exception {

        doAddSubjectIndicator("http://semantag.org/si",john, true);
        
    }
    
    // test adding a subject indicator to a topic that is 
    // defined by a topic explicitly set
    public void testAddSubjectIndicatorToImplicitTopic() throws Exception {

        doAddSubjectIndicator("http://semantag.org/si",john, false);
    }

    
    // tests that specifying a topic explicitly via
    // the topic attribute has priority before a parent tag
    public void testImplicitAndExplicitTopic() throws Exception {

        // set the parent-tag
        // that shall remain unaltered in this test
        UseTopicTag utt = new UseTopicTag();
        utt.setTopic(john);
        asit.setParent(utt);
        
        // store subject indicator count of john
        int co = john.getSubjectIndicators().size();
        
        // specify topic explicitly
        Topic nina = tm.getTopicByID("nina");
        
        // add the subject indicator to nina
        doAddSubjectIndicator("http://semantag.org/si",nina, true);
        
        // assert that john remained unaltered
        assertEquals(co, john.getSubjectIndicators().size());
        
        
    }
    
    // tests that specifying no topic throws the exception expected
    public void testTagWithoutContext() throws Exception {
        

        try{
            doAddSubjectIndicator("http://semantag.org/si",null, true);
        }
        catch(JellyTagException ex){
            String expected = "AddSubjectIndicator must be either the children of an object ";
            assertTrue(ex.getReason().startsWith(expected));
        }
    }
    

    // tests that specifying no locator adress throws the exception expected
    public void testTagWithoutLocatorAdress() throws Exception {
        

        try{
            doAddSubjectIndicator("", john, true);
        }
        catch(JellyTagException ex){
            String expected = "Unable to create a Locator for adress: ";
            assertTrue(ex.getReason().startsWith(expected));
        }
    }
    
    // tests that specifying a locator adress 
    // that is already a subject indicator of this topic
    // throws the exception expected
//    public void testTagWithDuplicateLocatorAdress() throws Exception {
//        
//        String adress =" http://semantag.org/si";
//        doAddSubjectIndicator(adress, john, true);
//        
//        try{
//            doAddSubjectIndicator(adress, john, true);
//        }
//        catch(JellyTagException ex){
//            String expected = "Unable to create a Locator for adress: ";
//            assertTrue(ex.getReason().startsWith(expected));
//        }
//    }

    
    
    /**
     * Adds a basename with the given attributes.
     * Tests various properties
     * @param id
     * @param sl
     * @param name
     * @param parent a topic that is expected to be the parent of 
     * the basename
     * @throws Exception
     */
    private void doAddSubjectIndicator(String adress, Topic parent, boolean parentIsExplicit)
        throws Exception
    {

        addCount++;
        
        // set locator
        asit.setLocator(adress);
        
        // set the parent
        if(parent != null){
            if(parentIsExplicit){
                asit.setTopic(parent);
            }
            else {
                // ... implicit as tag
                UseTopicTag utt = new UseTopicTag();
                utt.setTopic(parent);
                asit.setParent(utt);
            }
        }
        

        // add a dummy body
        setScriptForTagBody(asit);

        // store subject indicator count before
        int co = 0;
        if(parent != null) co = parent.getSubjectIndicators().size();
        
        // add subject indicator 
        asit.doTag(null);

        // assert that a new subject indicator  was created
        assertEquals(co + 1, parent.getSubjectIndicators().size());

        // assert that there is a subject indicator  with the
        // adress specified
        boolean found = false;
        for (Iterator iter = parent.getSubjectIndicators().iterator(); iter.hasNext();) {
            Locator loc = (Locator) iter.next();
            if(loc.getAddress().equals(adress)){
                found = true;
                break;
            }
        }
        assertTrue(found);
        
                
        // assert that the body was not called
        assertFalse(scriptWasCalled);

    }

 


    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM,
                TestData.TM_JOHN_BASELOCATOR);

        asit = new AddSubjectIndicatorTag();
        ctx = new JellyContext();
        asit.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        john = tm.getTopicByID("john");

    }

}