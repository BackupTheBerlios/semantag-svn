package net.sf.semantag.tm;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 09.09.2004
 */
public class UseBaseNameTagTest extends TMTagTestBase {

    private TopicMap tm;

    private JellyContext ctx;

    protected String baseLoc = TestData.TM_GREEKS_BASELOCATOR;

    private UseBasenameTag tag;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tag = new UseBasenameTag();
        tm = getTopicMapFromResource(TestData.TM_GREEKS_XTM, baseLoc);

        ctx = new JellyContext();
        tag.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

    }

    //  test that all initial values are as expected
    public void testCreation() throws Exception {
        assertNull(tag.getSourceLocator());
        assertNull(tag.getId());
        assertNull(tag.getVar());
        assertNull(tag.getTmVar());

    }

    // test resolving the basename by id
    public void testResolveByID() throws Exception {

        String id = "name_of_maia";
        tag.setId(id);

        setScriptForTagBody(tag);

        // try to resolve basename
        tag.doTag(null);

        // assert that the
        // basename returned is the one expected
        BaseName bn = tag.getBaseName();
        Locator sl = (Locator) bn.getSourceLocators().iterator().next();
        assertEquals(baseLoc + "#" + id, sl.getAddress());

        // assert that the body was called
        assertTrue(scriptWasCalled);
    }

    // test that a basename can be retrieved by sourceLocator
    public void testResolveBySourceLocator() throws Exception {

        String id = "name_of_elektra";
        String adress = baseLoc + "#" + id;
        tag.setSourceLocator(adress);

        setScriptForTagBody(tag);

        // try to resolve basename
        tag.doTag(null);

        // assert that the
        // basename returned is the one expected
        BaseName bn = tag.getBaseName();
        assertEquals(id, bn.getID());

        // assert that the body was called
        assertTrue(scriptWasCalled);

    }

    // test that a basename that is stored in
    // the context can be retrieved by variablename
    public void testResolveByVariable() throws Exception {

        String varname = "BASENAME";
        String id = "name_of_orest";

        setScriptForTagBody(tag);

        // get the basename and store it in the context
        BaseName bn = (BaseName) tm.getObjectByID(id);
        assertNotNull(bn);
        ctx.setVariable(varname, bn);

        // set the variable that shall be lookuped
        tag.setFromVar(varname);

        // try to resolve basename
        tag.doTag(null);

        assertEquals(bn, tag.getBaseName());

        // assert that the body was called
        assertTrue(scriptWasCalled);
    }

    // test the case that the resolvement fails
    // because of an unknown id
    public void testNonExistanceID() throws Exception {

        String id = " does_not_exist";
        tag.setId(id);
        setScriptForTagBody(tag);

        // resolving
        resolveNonExistantObject(tag);

        // basename should be null
        assertNull(tag.getBaseName());

    }

    // test the case that the resolvement fails
    // because of an unknown sourceLocator
    public void testNonExistanceSourceLocator() throws Exception {

        String sl = "does_not_exist";
        String adress = "fooo  -" + sl;
        tag.setSourceLocator(adress);

        // resolving
        resolveNonExistantObject(tag);

        // basename should be null
        assertNull(tag.getBaseName());

    }

    // test the case that the resolvement fails
    // because of a sourceLocator that points to
    // an object that is not of type Basename
    public void testSourceLocatorToNonBasenameObject() throws Exception {

        String adress = baseLoc + "#Helena";
        tag.setSourceLocator(adress);

        // resolving
        try {
            resolveNonExistantObject(tag);
            fail("Expected exception since sourceLocator points to another object");
        } catch (JellyTagException e) {
            // expected
        }

    }

    // test the case that the resolvement fails
    // because of an unknown variable
    public void testNonExistantVariable() throws Exception {

        String var = "not bound";
        tag.setFromVar(var);

        // resolving
        resolveNonExistantObject(tag);

        // basename should be null
        assertNull(tag.getBaseName());

    }

    // test the case that the resolvement fails
    // because of a variable that is bound to an
    // object other than a basename
    public void testVariableWithFalseObject() throws Exception {

        String var = "TOPIC";
        Topic t = tm.getTopicByID("Helena");
        assertNotNull(t);
        ctx.setVariable(var, t);
        tag.setFromVar(var);

        // resolving
        try {
            resolveNonExistantObject(tag);
            fail("Expected Exception since the variable is bound to another object");
        } catch (JellyTagException e) {
            // expected
        }

    }

    // test resolvement failure
    // with mode FAIL
    public void testNonExistanceIDWithFAIL() throws Exception {

        String id = " does_not_exist";
        tag.setId(id);
        tag.setNonexistant(BaseUseTag.NE_FAIL);
        setScriptForTagBody(tag);

        // resolving
        try {
            tag.doTag(null);
            fail("Expected Exception since the variable is bound to another object");
        } catch (JellyTagException e) {
            // expected
        }

    }

    // test resolvement failure
    // with mode ADD
    public void testNonExistanceIDWithADD() throws Exception {

        String id = "does_not_exist";
        tag.setId(id);
        tag.setNonexistant(BaseUseTag.NE_ADD);
        setScriptForTagBody(tag);

        // make the parent to which the basename will be added
        UseTopicTag utt = new UseTopicTag();
        utt.setId("Helena");
        utt.setContext(ctx);

        // set the UseTopicTag as parent of the
        // UseBaseNameTag
        tag.setParent(utt);

        Topic helena = utt.getTopic();
        int co = helena.getNames().size();

        // resolving
        // this should lead to the creation
        // of a new basename in topic Helena
        tag.doTag(null);
        assertEquals(co + 1, helena.getNames().size());

        BaseName bn = tag.getBaseName();
        assertEquals(bn.getParent(), helena);

    }

    // helper that executes doTag(null) and
    // asserts that the body of the tag was not executed
    private void resolveNonExistantObject(BaseUseTag tag) throws Exception {
        setScriptForTagBody(tag);

        // resolvement should fail silently
        tag.setNonexistant(BaseUseTag.NE_IGNORE_BODY);

        // try to resolve basename
        tag.doTag(null);

        // basename should not have been called
        assertFalse(scriptWasCalled);
    }
}