package org.semantag.tm;


import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.semantag.TestData;
import org.semantag.tm.AddBasenameTag;
import org.semantag.tm.Dictionary;
import org.semantag.tm.UseTopicTag;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class AddBasenameTagTest extends TMTagTestBase {

    private TopicMap tm;

    private AddBasenameTag abt;

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
    public void testAddBasenameToParentTag() throws Exception {

        doAddBasename(null, null, null,john, false);
        
    }
    
    // tests that specifying a topic explicitly via
    // the topic attribute has priority before a parent tag
    public void testAddBasenameToTopicSpecifiedExplicitly() throws Exception {

        // set the parent-tag
        // that shall remain unaltered in this test
        UseTopicTag utt = new UseTopicTag();
        utt.setTopic(john);
        abt.setParent(utt);
        
        // store name count of john
        int co = john.getNames().size();
        
        // specify topic explicitly
        Topic nina = tm.getTopicByID("nina");
        
        // add the basename to nina
        doAddBasename(null, null, null, nina, true);
        
        // assert that john remained unaltered
        assertEquals(co, john.getNames().size());
        
        
    }
    
    public void testAddWithExistingID() throws Exception {
        
        
        // store basename count before
        int co = john.getNames().size();

        // add basename for an existing id
        try {
            doAddBasename(personID,null, null, john, true);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
            assertEquals(e.getCause().getClass(), DuplicateObjectIDException.class);
        }

        // assert that no new basename was created
        assertEquals(co, john.getNames().size());

    }
    

    public void testAddWithExistingSourceLocator() throws Exception {

        // any existing sourceLocator
        Locator sl = (Locator) tm.getTopicByID(personID).getSourceLocators().iterator().next();

        // store basename count before
        int co = john.getNames().size();

        // addTopic
        try {
            doAddBasename(null ,sl.getAddress(), null, john, true);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
            assertTrue(e.getReason().startsWith("The topicmap already contains a sourceLocator"));
        }

        // assert that no new basename was created
        assertEquals(co, john.getNames().size());

    }
    
    /**
     * adding a basename without a name should fail
     *
     */
    public void testEmptyName(){
        // addTopic
        try {
            doAddBareBasename(null ,null, null, john, true, null);
            fail("Expected Exception for creating a basename without a name");
        } catch (Exception e) {
            // expected
        }
       
        // addTopic
        try {
            doAddBareBasename(null ,null, "", john, true, null);
            fail("Expected Exception for creating a basename with an empty name");
        } catch (Exception e) {
            // expected
        }
    }
    
    /**
     * Helper for the testcases
     * Adds a basename with the given attributes.
     * Tests various properties
     * @param id
     * @param sl
     * @param name
     * @param parent a topic that is expected to be the parent of 
     * the basename
     * @throws Exception
     */
    private void doAddBasename(String id, String sl, String name, Topic parent, boolean parentIsExplicit)
        throws Exception
    {

        addCount++;
        
        if(id == null) id = "basename_nonexistant_id"+addCount;
        if(sl == null) sl = "http://non.existant.org/"+addCount;
        if(name == null) name ="Name for new Basename"+addCount;

        String varName = "BASENAME";
        doAddBareBasename(id, sl, name, parent, parentIsExplicit, varName);
    }
    
    private void doAddBareBasename(String id, String sl, String name, 
            Topic parent, boolean parentIsExplicit, String varName)
        throws Exception
    {
        // set id and sourcelocator
        abt.setId(id);
        abt.setSourceLocator(sl);

        // set name
        abt.setName(name);
        
        // set the parent
        if(parentIsExplicit){
            abt.setTopic(parent);
        }
        else {
            // ... as tag
            UseTopicTag utt = new UseTopicTag();
            utt.setTopic(parent);
            abt.setParent(utt);
        }
        
        // set the name of the variable
        // that shall store the basename
        abt.setVar(varName);

        // add a dummy body
        setScriptForTagBody(abt);

        // store names count before
        int co = parent.getNames().size();

        // add basename
        abt.doTag(null);

        // assert that a new basename was created
        assertEquals(co + 1, parent.getNames().size());

        // assert that there is a basename with the
        // id and the sourceLocator specified
        BaseName bn = (BaseName)tm.getObjectByID(id);
        assertEquals(sl, ((Locator) bn.getSourceLocators().iterator().next())
                .getAddress());

        // assert name 
        assertEquals(name, bn.getData());
        
        // assert that the basename is retrievable
        // via the ContextBasename-API
        assertEquals(bn, abt.getBaseName());
        
        // assert that the parent is the one expected
        assertEquals(parent, bn.getParent());
        
        // assert that the basename is retrievable
        // via the context variable
        if(varName != null)
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

        abt = new AddBasenameTag();
        ctx = new JellyContext();
        abt.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        john = tm.getTopicByID("john");

    }

}