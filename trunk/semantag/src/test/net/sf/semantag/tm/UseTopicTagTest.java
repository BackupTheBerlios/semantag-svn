package net.sf.semantag.tm;

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
public class UseTopicTagTest extends BaseTMTagTest {
    
    private TopicMap tm;
    private UseTopicTag utt;
    private JellyContext ctx;
    
    // an often used topic
    private Topic john; 
    
    
    // test that looking for a nonexistant
    // topic fails with an exception (if specified)
    public void testShallFailOnNonexistant() {
        utt.setNonexistant("fail");
        try{
            utt.doTag(null);
            fail("Did expect an exception");
        }catch(JellyTagException e){
            // expected. ok
        }
    }

    // test that looking for a nonexistant
    // topic adds a new one (if specified)
    public void testShallAddOnNonexistant() 
        throws Exception
    {
        utt.setNonexistant("add");

        // set a script for the body of the tag
        scriptWasCalled = false;
        setScriptForTagBody(utt);
        
        int co = tm.getTopicCount();
        utt.doTag(null);
        // there should be one more topic
        assertEquals(co +1, tm.getTopicCount());
        
        // the body script should have been called
        assertTrue(scriptWasCalled);
    }

    // test that looking for a nonexistant
    // topic ignores the body of the tag (if specified)
    public void testShallIgnoreOnNonexistant() 
        throws Exception
    {

        utt.setNonexistant("ignore");
        int co = tm.getTopicCount();

        // set a script for the body of the tag
        scriptWasCalled = false;
        setScriptForTagBody(utt);
        
        utt.doTag(null);
        // there should be the same topic count
        assertEquals(co , tm.getTopicCount());
        
        // the body should have been ignored
        assertFalse(scriptWasCalled);
    }

    
    // test the connection between tag.topicvar 
    // and the TopicResolver delegate
    public void testSetTopicVar() 
        throws Exception
    {
        
        String topicvar="TOPIC";
        
        // store topic in context
        ctx.setVariable(topicvar, john);

        // assert that the resolver is uninitialized
        assertNull(utt.getTopic());
        
        // resolver shall resolve by variable
        utt.setTopicVar(topicvar);
        
        assertEquals(john, utt.getTopic());
    }

    // test the connection between tag.topicID 
    // and the TopicResolver-delegate
    public void testSetTopicID() 
        throws Exception
    {
        
        // assert that the resolver is uninitialized
        utt.setNonexistant("ignore");
        assertNull(utt.getTopic());

        // resolver shall resolve by variable
        utt.setTopicID("john");
        
        assertEquals(john, utt.getTopic());
    }
        
    public void testSetTopicName() 
        throws Exception
    {
        
    }

    // test the connection between useTopicTag.topicSourceLocator
    // and the TopicResolver-delegate
    public void testSetTopicSourceLocator() 
        throws Exception
        {
            // assert that the resolver is uninitialized
            utt.setNonexistant("ignore");
            assertNull(utt.getTopic());

            // resolver shall resolve by variable
            utt.setTopicSourceLocator(TestData.TM_JOHN_BASELOCATOR+"#john");
            
            assertEquals(john, utt.getTopic());
            
        }

    // test the connection between useTopicTag.topicSubject
    // and the TopicResolver-delegate
    public void testSetTopicSubject() 
        throws Exception
    {
        // assert that the resolver is uninitialized
        utt.setNonexistant("ignore");
        assertNull(utt.getTopic());

        // resolver shall resolve by variable
        utt.setTopicSubject("http://www.about_john.org");
        
        assertEquals("johns_site", utt.getTopic().getID());
        
    }

    // test the connection between useTopicTag.topicSubjectIndicator
    // and the TopicResolver-delegate
    public void testSetTopicSubjectIndicator() 
        throws Exception
    {
        // assert that the resolver is uninitialized
        utt.setNonexistant("ignore");
        assertNull(utt.getTopic());

        // resolver shall resolve by variable
        utt.setTopicSubjectIndicator("http://www.fiatpanda.it/");
        
        assertEquals("fiat-panda", utt.getTopic().getID());
    }
    
    // tests that the tm-property manages to 
    // switch between topicmaps
    public void testSetTmVar() 
        throws Exception
    {
        
        TopicMap tm2 = getTopicMapFromResource(TestData.TM_GREEKS_XTM, "http://greeks.org");
        // assert that the default topicmap does not
        // contain the topic with id Helena
        utt.setNonexistant("ignore");
        utt.setTopicID("Helena");
        assertNull(utt.getTopic());
        
        // now set the topicmap via the tmvar-property
        utt.setTmVar("TOPICMAP");
        ctx.setVariable("TOPICMAP",tm2);
        assertEquals("Helena",utt.getTopic().getID());
        
        
    }

    // the id-Property of this Tag
    // should be redirected to the topicID-Property
    public void testSetId() 
        throws Exception
    {
        // assert that the resolver is uninitialized
        utt.setNonexistant("ignore");
        assertNull(utt.getTopic());

        // resolver shall resolve by variable
        utt.setId("john");
        
        assertEquals(john, utt.getTopic());

    
    }

    public void testSetSourceLocator() 
        throws Exception
    {
        
        // assert that the resolver is uninitialized
        utt.setNonexistant("ignore");
        assertNull(utt.getTopic());

        // resolver shall resolve by variable
        utt.setSourceLocator(TestData.TM_JOHN_BASELOCATOR+"#john");
        
        assertEquals(john, utt.getTopic());

    }
    
    // test that when adding a topic
    // id and sourcelocator will be set
    public void testAddTopic()
        throws Exception
    {
        utt.setNonexistant("add");

        // set a script for the body of the tag
        setScriptForTagBody(utt);
        
        int co = tm.getTopicCount();
        utt.setTopicID("newTopic");
        utt.setSourceLocator("http://source.loc");
        utt.doTag(null);
        // there should be one more topic
        assertEquals(co +1, tm.getTopicCount());
        Topic t = tm.getTopicByID("newTopic");
        
        assertEquals("http://source.loc", ((Locator)t.getSourceLocators().iterator().next()).getAddress());
        
        // the body script should have been called
        assertTrue(scriptWasCalled);
    
    }
 
    // test that an eventually created topic will be
    // created in the correct map
    public void testAddATopicToASpecificMap()
        throws Exception
     {
        
        TopicMap tm2 = getTopicMapFromResource(TestData.TM_GREEKS_XTM, "http://greeks.org");
        
        utt.setNonexistant("add");

        // set a script for the body of the tag
        setScriptForTagBody(utt);
        String tmvar = "TOPICMAP";
        utt.setTmVar(tmvar);
        ctx.setVariable(tmvar, tm2);
        
        // store topic counts for each map
        int co2 = tm2.getTopicCount();
        int co = tm.getTopicCount();
        
        utt.setTopicID("id_for_nonexistant_Topic");
        utt.doTag(null);
        
        // tm2 should have a new topic, 
        // the topic count of tm should remain the same
        assertEquals(co2 +1, tm2.getTopicCount());
        assertEquals(co, tm.getTopicCount());
        
        
    }
    
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM, TestData.TM_JOHN_BASELOCATOR);

        utt = new UseTopicTag();
        ctx = new JellyContext();
        utt.setContext(ctx);
        
        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        john = tm.getTopicByID("john");
    
    }


}
