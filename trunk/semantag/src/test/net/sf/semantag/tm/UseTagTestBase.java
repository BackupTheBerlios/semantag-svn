package net.sf.semantag.tm;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

/**
 * 
 * @author cf
 * @version 0.1, created on 10.09.2004
 */
public abstract class UseTagTestBase extends TMTagTestBase {

    protected TopicMap tm;

    protected TopicMap tm2;

    protected JellyContext ctx;

    protected String baseLoc = TestData.TM_GREEKS_BASELOCATOR;

    protected String baseLoc2 = "http://foo2.org";

    // an id that is not a bound if in the tested topicmap
    protected String nonexistantId = " does_not_exist";

    protected String nonexistantSL = "foo.org#bar";

    /**
     *  
     */
    public UseTagTestBase() throws Exception{
        this(null);
    }

    /**
     * @param name
     */
    public UseTagTestBase(String name) throws Exception {
        super(name);
        tm = getTopicMapFromResource(TestData.TM_GREEKS_XTM, baseLoc);
        tm2 = getTopicMapFromResource(TestData.TM_TEST2_XTM, baseLoc2);
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        ctx = new JellyContext();
        getTag().setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

    }

    // tests the default values of a new instance
    public void testCreation() throws Exception {
        BaseUseTag tag = getTag();
        assertNull(tag.getSourceLocator());
        assertNull(tag.getId());
        assertNull(tag.getVar());
        assertNull(tag.getTmVar());

    }

    // tests the case that a sourceLocator points to
    // an object that is not of expected type
    public void testSourceLocatorToFalseObject() throws Exception {

        BaseUseTag tag = getTag();
        String adress = baseLoc + "#" + getIDForWrongType();
        tag.setSourceLocator(adress);

        // resolving
        try {
            resolveNonExistantObject(tag);
            fail("Expected exception since sourceLocator points to another object");
        } catch (JellyTagException e) {
            // expected
        }

    }

    // tests the case that a variable points to
    // an object that is not of expected type
    public void testVariableWithFalseObject() throws Exception {

        BaseUseTag tag = getTag();

        // setup a variable that points to an
        // Object of a type that does not fit
        // with the current tag
        String var = "aVariable";
        Object t = tm.getObjectByID(getIDForWrongType());
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

    // checks resolving by id for the case that
    // no topic map object for the given id does exist
    public void testNonExistanceIDWithFAIL() throws Exception {

        BaseUseTag tag = getTag();
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

    // helper
    // executes the doTag(...) - method of the given tag
    // asserts that the body of the tag was not called
    protected void resolveNonExistantObject(BaseUseTag tag) throws Exception {
        setScriptForTagBody(tag);

        // resolvement should fail silently
        tag.setNonexistant(BaseUseTag.NE_IGNORE_BODY);

        // try to resolve basename
        tag.doTag(null);

        // basename should not have been called
        assertFalse(scriptWasCalled);
    }

    // ---------------------------------------------------------------------
    // To be called from the extending classes

    // test resolving the basename by id
    public void checkResolveByID(String id) throws Exception {

        getTag().setId(id);

        // executes doTag(...) and asserts that
        // the body of the tag gets called.
        checkSuccessfullDoTagExecution();

        // assert that the
        // object returned is the one expected
        TopicMapObject tmo = getTag().resolve();
        Locator sl = (Locator) tmo.getSourceLocators().iterator().next();
        assertEquals(baseLoc + "#" + id, sl.getAddress());

    }

    public void checkResolveByIDInMap(String id, TopicMap tm) throws Exception {

        // store topicmap in context
        String varName = "TM2";
        ctx.setVariable(varName, tm);
        getTag().setTmVar(varName);

        // set id
        getTag().setId(id);

        // executes doTag(...) and asserts that
        // the body of the tag gets called.
        checkSuccessfullDoTagExecution();

        // assert that the
        // object returned is the one expected
        String bl = tm.getBaseLocator().getAddress();
        TopicMapObject tmo = getTag().resolve();
        Locator sl = (Locator) tmo.getSourceLocators().iterator().next();
        assertEquals(bl + "#" + id, sl.getAddress());

    }

    // test that a topicmapobject that is stored in
    // the context can be retrieved by variablename
    public void checkResolveByVariable(String id, TopicMapObject expected)
            throws Exception {

        String varname = "BASENAME";

        setScriptForTagBody(getTag());

        // store the expected object as value of the
        // variable in the context
        assertNotNull(expected);
        ctx.setVariable(varname, expected);

        // set the variable that shall be lookuped
        getTag().setFromVar(varname);

        // try to resolve the object
        getTag().doTag(null);

        // assert that the body was called
        assertTrue(scriptWasCalled);
    }

    // test the case that the resolvement fails
    // because of an unknown id
    public void checkResolveNonExistanceID() throws Exception {

        getTag().setId(nonexistantId);
        setScriptForTagBody(getTag());

        // resolving
        resolveNonExistantObject(getTag());

    }

    // test the case that the resolvement fails
    // because of an unknown sourceLocator
    public void checkNonExistantSourceLocator() throws Exception {

        getTag().setSourceLocator(nonexistantSL);

        // resolving
        resolveNonExistantObject(getTag());
    }

    // test the case that the resolvement fails
    // because the tag refers to a variable
    // that is not set in the context
    public void checkNonExistantVariable() throws Exception {

        String var = "not bound";
        getTag().setFromVar(var);

        // resolving
        resolveNonExistantObject(getTag());

    }

    // executes the doTag()-Method of the tag
    // and asserts that the body of the tag was called
    public void checkSuccessfullDoTagExecution() throws Exception {
        // add a body to the tag
        setScriptForTagBody(getTag());

        // execute
        getTag().doTag(null);

        // assert that the body was called
        assertTrue(scriptWasCalled);
    }

    /**
     * @return an instance of the Class that is under test
     */
    protected abstract BaseUseTag getTag();

    /**
     * 
     * @return an id, that points to a topic map object in the current map. The
     *         topic map object must *NOT* be of the type that the UseTag-class
     *         under test is able to resolve
     */
    protected abstract String getIDForWrongType();
}