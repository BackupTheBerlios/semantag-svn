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
public class AddTopicTagTest extends BaseTMTagTest {

    private TopicMap tm;

    private AddTopicTag att;

    private JellyContext ctx;

    // an often used topic
    private Topic john;

    public void testAddTopic() throws Exception {
        String newid = "nonexistant_id";
        String sl = "http://non.existant.org";

        // set id and sourcelocator
        att.setId(newid);
        att.setSourceLocator(sl);

        // add a dummy body
        setScriptForTagBody(att);

        // store topic count before
        int co = tm.getTopicCount();

        // addTopic
        att.doTag(null);

        // assert that a new topic was created
        assertEquals(co + 1, tm.getTopicCount());

        // assert that there is a topic with the
        // id and the sourceLocator specified
        Topic t = tm.getTopicByID(newid);
        assertEquals(sl, ((Locator) t.getSourceLocators().iterator().next())
                .getAddress());

        // assert that the topic is retrievable
        // via the topic context api
        assertEquals(t, att.getTopic());
        
        // assert that the body was called
        assertTrue(scriptWasCalled);

    }

    public void testAddTopicWithDuplicateID() throws Exception {
        // set existing id
        att.setId(john.getID());

        // store topic count before
        int co = tm.getTopicCount();

        // addTopic
        try {
            att.doTag(null);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
        }

        // assert that no new topic was created
        assertEquals(co, tm.getTopicCount());

    }

    public void testAddTopicWithDuplicateSourceLocator() throws Exception {
        // set existing id
        Locator sl = (Locator) john.getSourceLocators().iterator().next();
        att.setSourceLocator(sl.getAddress());

        // store topic count before
        int co = tm.getTopicCount();

        // addTopic
        try {
            att.doTag(null);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
        }

        // assert that no new topic was created
        assertEquals(co, tm.getTopicCount());

    }
    
    
    // tests adding a topic to a topicmap other than
    // the default map
    public void testAddTopicToTMSpecified() throws Exception {
        String newid = "nonexistant_id";
        TopicMap tm2 = getTopicMapFromResource(TestData.TM_GREEKS_XTM, "greek.org");
        
        // set id 
        att.setId(newid);

        // set tm to process on
        att.setTmVar("TOPICMAP");
        ctx.setVariable("TOPICMAP", tm2);
        
        // add a dummy body
        setScriptForTagBody(att);

        // store topic count before
        int co = tm.getTopicCount();
        int co2 = tm2.getTopicCount();

        // addTopic
        att.doTag(null);

        // assert that a new topic was created
        // in tm2
        assertEquals(co2 + 1, tm2.getTopicCount());

        // assert that tm1 remains untouched
        assertEquals(co, tm.getTopicCount());
        
        Topic created = att.getTopic();
        assertEquals(tm2, created.getTopicMap());

    }

    // tests that the new topic is accessible
    // under the variable specified
    public void testStoreNewTopicInVariable() throws Exception {

        String newid = "nonexistant_id";
        
        // set id 
        att.setId(newid);

        // set varname
        att.setVar("TOPIC");

        
        // add a dummy body
        setScriptForTagBody(att);

        // addTopic
        att.doTag(null);

        // assert that the topic is accessible
        // via the getTopic()-Method as well
        // as via the variable specified
        Topic created = att.getTopic();
        assertEquals(ctx.getVariable("TOPIC"),created);

    }



    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM,
                TestData.TM_JOHN_BASELOCATOR);

        att = new AddTopicTag();
        ctx = new JellyContext();
        att.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        john = tm.getTopicByID("john");

    }

}