package org.semantag.tm;


import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.semantag.TestData;
import org.semantag.tm.Dictionary;
import org.semantag.tm.UseTopicTag;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class UseTopicTagTest extends TMTagTestBase {
    
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
    
    // test that the body of the tag is skipped
    // when tag is in non-existant-mode "add"
    // and the topicmap object exists already
    public void testADDForExistant() 
        throws Exception
    {
        utt.setNonexistant("add");
        utt.setTopic(john);
        
        // set a script for the body of the tag
        scriptWasCalled = false;
        setScriptForTagBody(utt);
        
        int co = tm.getTopicCount();
        utt.doTag(null);
        // there should be the same topic count
        assertEquals(co, tm.getTopicCount());
        
        // the body script must not have been called
        assertFalse(scriptWasCalled);
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

  
    
    // test setting a topic directly
    public void testSetTopic() 
        throws Exception
    {
        
        // sets topic directly
        utt.setTopic(john);
        
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
        utt.setTopicmap(tm2);
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
        
        // the topic created shall be returned by a call
        // to the getTopicMethod
        assertEquals(t, utt.getTopic());
    
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
        utt.setTopicmap(tm2);
        
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

    // tests that a resolved topic is bound to 
    // the variable specified by the var-property
    // tests secondly that a variable is reset to 
    // null, if the topic could not ne found
    public void testTopicIsBoundToVariable()
        throws Exception
    {
        
        // resolver shall resolve by id
        utt.setTopicID("john");

        // resolved topic shall be stored in
        // the variable named TOPIC
        utt.setVar("TOPIC");
        
        // resolve
        setScriptForTagBody(utt);
        utt.doTag(null);
        
        // the topic should be stored in the variable
        assertEquals(john, ctx.getVariable("TOPIC"));
        
        // make a new tag, bound to the same variable 
        // set resolvement to non existant topic
        utt = new UseTopicTag();
        utt.setContext(ctx);
        utt.setVar("TOPIC");
        utt.setTopicID("nojohn");

        // resolve
        utt.doTag(null);

        // the variable should be resetted
        assertNull(ctx.getVariable("TOPIC"));
        
        
    }
    
    // tests that a created topic is bound to 
    // the variable specified by the var-property
    public void testCreatedTopicIsBoundToVariable()
        throws Exception
    {
        
        // tag shall create a topic
        utt.setNonexistant("add");
        utt.setTopicID("nonexistant_id");

        // resolved topic shall be stored in
        // the variable named TOPIC
        utt.setVar("TOPIC");
        
        // create
        setScriptForTagBody(utt);
        utt.doTag(null);
        
        // the topic should be stored in the variable
        assertEquals("nonexistant_id", ((Topic)ctx.getVariable("TOPIC")).getID());
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
