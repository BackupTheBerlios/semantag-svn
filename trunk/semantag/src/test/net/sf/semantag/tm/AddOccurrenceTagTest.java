package net.sf.semantag.tm;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class AddOccurrenceTagTest extends TMTagTestBase {

    private TopicMap tm;

    private AddOccurrenceTag aot;

    private JellyContext ctx;

    // an often used topic
    private Topic john;

    // an existing id 
    final String personID  = "tt-person";
    
    // used to generate unique ids for every  
    // basename added.
    private static int addCount = 0;
    
    // test adding a basename to a topic that is 
    // defined by a parent ContextTopic
    public void testAddOccurenceToImplicitTopic() throws Exception {

        doAddOccurrence(null, null, "some data", null,john, false);
        
    }
    
    // tests that specifying a topic explicitly via
    // the topic attribute works
    public void testAddOccurenceToExplicitTopic() throws Exception {

        doAddOccurrence(null, null, "some data", null,john, false);
    }

    
    // tests that specifying a topic explicitly via
    // the topic attribute has priority before a parent tag
    public void testImplicitAndExplicitTopic() throws Exception {

        // set the parent-tag
        // that shall remain unaltered in this test
        UseTopicTag utt = new UseTopicTag();
        utt.setTopic(john);
        aot.setParent(utt);
        
        // store occurrence count of john
        int co = john.getOccurrences().size();
        
        // specify topic explicitly
        Topic nina = tm.getTopicByID("nina");
        
        // add the occurrence to nina
        doAddOccurrence(null, null, "hi", null, nina, true);
        
        // assert that john remained unaltered
        assertEquals(co, john.getOccurrences().size());
        
        
    }
    
    // tests that specifying a topic explicitly via
    // the topic attribute works
    public void testDataAndResource() throws Exception {

        // add with data
        doAddOccurrence(null, null, "some data", null,john, true);

        // add with resource
        doAddOccurrence(null, null, null, "http:/semantag.org/occ",john, false);

        // add with both
        doAddOccurrence(null, null, "data", "http:/semantag.org/occ",john, false);

        // add with neither
        try{
            doAddOccurrence(null, null, "data", "http:/semantag.org/occ",john, false);
        }
        catch(MissingAttributeException ex){
            assertEquals("'data' or 'resource'",ex.getMissingAttribute());
        }
    }
    
    
    // tests the tag outside of the context of a topic
    public void testMissingParent() throws Exception {


        try{
            doAddOccurrence(null, null, "data", "http:/semantag.org/occ",null, false);
        }
        catch(JellyTagException e){
            String expected = "AddOccurrence must be either the children of an object ";
            assertTrue(e.getReason().startsWith(expected));
        }
    }
    
    public void testAddWithExistingID() throws Exception {
        
        
        // store occurrence count before
        int co = john.getOccurrences().size();

        // add occurrence for an existing id
        try {
            doAddOccurrence(personID,null,"some data", null, john, true);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
            assertEquals(e.getCause().getClass(), DuplicateObjectIDException.class);
        }

        // assert that no new occurrence was created
        assertEquals(co, john.getOccurrences().size());

    }
    

    public void testAddWithExistingSourceLocator() throws Exception {

        // any existing sourceLocator
        Locator sl = (Locator) tm.getTopicByID(personID).getSourceLocators().iterator().next();

        // store occurrence count before
        int co = john.getOccurrences().size();

        // addTopic
        try {
            doAddOccurrence(null ,sl.getAddress(), "some", null, john, true);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
            assertTrue(e.getReason().startsWith("The topicmap already contains a sourceLocator"));
        }

        // assert that no new basename was created
        assertEquals(co, john.getOccurrences().size());

    }
    
    
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
    private void doAddOccurrence(String id, String sl, String name, String resource, Topic parent, boolean parentIsExplicit)
        throws Exception
    {

        addCount++;
        
        if(id == null) id = "occ_nonexistant_id"+addCount;
        if(sl == null) sl = "http://non.existant.org/occ/"+addCount;
        
        String varName = "OCC";
        
        // set id and sourcelocator
        aot.setId(id);
        aot.setSourceLocator(sl);

        // set data and resource
        aot.setData(name);
        aot.setResource(resource);
        
        // set the parent
        if(parent != null){
            if(parentIsExplicit){
                aot.setTopic(parent);
            }
            else {
                // ... implicit as tag
                UseTopicTag utt = new UseTopicTag();
                utt.setTopic(parent);
                aot.setParent(utt);
            }
        }
        
        // set the name of the variable
        // that shall store the occurrence
        aot.setVar(varName);

        // add a dummy body
        setScriptForTagBody(aot);

        // store occurrence count before
        int co = 0;
        if(parent != null) co = parent.getOccurrences().size();
        
        // add occurrence
        aot.doTag(null);

        // assert that a new occurrence was created
        assertEquals(co + 1, parent.getOccurrences().size());

        // assert that there is a occurrence with the
        // id and the sourceLocator specified
        Occurrence bn = (Occurrence)tm.getObjectByID(id);
        assertEquals(sl, ((Locator) bn.getSourceLocators().iterator().next())
                .getAddress());

        // assert data or resource
        if(name != null)assertEquals(name, bn.getData());
        else assertEquals(resource, bn.getDataLocator().getAddress());
        
        // assert that the occurrence is retrievable
        // via the ContextOccurrence-API
        assertEquals(bn, aot.getOccurrence());
        
        // assert that the parent is the one expected
        assertEquals(parent, bn.getParent());
        
        // assert that the occurrence is retrievable
        // via the context variable
        assertEquals(bn, ctx.getVariable(varName));
        
        // assert that the body was called
        assertTrue(scriptWasCalled);

    }

 


    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM,
                TestData.TM_JOHN_BASELOCATOR);

        aot = new AddOccurrenceTag();
        ctx = new JellyContext();
        aot.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        john = tm.getTopicByID("john");

    }

}