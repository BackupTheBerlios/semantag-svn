package net.sf.semantag.tm;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class AddAssociationTagTest extends TMTagTestBase {

    private TopicMap tm;

    private AddAssociationTag aat;

    private JellyContext ctx;

    // an often used topic
    private Topic john;

    public void testAddAssociation() throws Exception {
        String newid = "nonexistant_id";
        String sl = "http://non.existant.org";

        // set id and sourcelocator
        aat.setId(newid);
        aat.setSourceLocator(sl);

        // add a dummy body
        setScriptForTagBody(aat);

        // store assoc count before
        int co = tm.getAssociations().size();

        // add association
        aat.doTag(null);

        // assert that a new association was created
        assertEquals(co + 1, tm.getAssociations().size());

        // assert that there is an association with the
        // id and the sourceLocator specified
        Association a = (Association)tm.getObjectByID(newid);
        assertEquals(sl, ((Locator) a.getSourceLocators().iterator().next())
                .getAddress());

        // assert that the association is retrievable
        // via the ContextAssociation API
        assertEquals(a, aat.getAssociation());
        
        // assert that the body was called
        assertTrue(scriptWasCalled);

    }

    public void testAddAssociationWithDuplicateID() throws Exception {
        // set existing id
        aat.setId(john.getID());

        // store association count before
        int co = tm.getAssociations().size();

        // add association 
        try {
            aat.doTag(null);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
        }

        // assert that no new association was created
        assertEquals(co, tm.getAssociations().size());

    }

    public void testAddAssociationWithDuplicateSourceLocator() throws Exception {
        // set existing id
        Locator sl = (Locator) john.getSourceLocators().iterator().next();
        aat.setSourceLocator(sl.getAddress());

        // store topic count before
        int co = tm.getAssociations().size();

        // addTopic
        try {
            aat.doTag(null);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
        }

        // assert that no new topic was created
        assertEquals(co, tm.getAssociations().size());

    }
    
    
    // tests adding an association to a topicmap other than
    // the default map
    public void testAddAssociationToTMSpecified() throws Exception {
        String newid = "nonexistant_id";
        TopicMap tm2 = getTopicMapFromResource(TestData.TM_GREEKS_XTM, "greek.org");
        
        // set id 
        aat.setId(newid);

        // set tm to process on
        aat.setTopicmap(tm2);
        
        // add a dummy body
        setScriptForTagBody(aat);

        // store association count before
        int co = tm.getAssociations().size();
        int co2 = tm2.getAssociations().size();

        // add association to tm2
        aat.doTag(null);

        // assert that a new association was created
        // in tm2
        assertEquals(co2 + 1, tm2.getAssociations().size());

        // assert that tm1 remains untouched
        assertEquals(co, tm.getAssociations().size());
        
        Association created = aat.getAssociation();
        assertEquals(tm2, created.getTopicMap());

    }

    // tests that the new association is accessible
    // through the variable specified
    public void testStoreNewAssociationInVariable() throws Exception {

        String newid = "nonexistant_id";
        
        // set id 
        aat.setId(newid);

        // set varname
        aat.setVar("ASSOC");

        
        // add a dummy body
        setScriptForTagBody(aat);

        // addTopic
        aat.doTag(null);

        // assert that the topic is accessible
        // via the getTopic()-Method as well
        // as via the variable specified
        Association created = aat.getAssociation();
        assertEquals(ctx.getVariable("ASSOC"),created);

    }



    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM,
                TestData.TM_JOHN_BASELOCATOR);

        aat = new AddAssociationTag();
        ctx = new JellyContext();
        aat.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        john = tm.getTopicByID("john");

    }

}