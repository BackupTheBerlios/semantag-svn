 package net.sf.semantag.tm;

import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Topic;

/**
 * 
 * @author cf
 * @version 0.1, created on 09.09.2004
 */
public class UseBaseNameTagTest extends UseTagTestBase {

    private UseBasenameTag tag;

    
    
    /**
     * @throws Exception
     */
    public UseBaseNameTagTest() throws Exception {
        super();
    }
    /**
     * @param name
     * @throws Exception
     */
    public UseBaseNameTagTest(String name) throws Exception {
        super(name);
    }

    protected BaseUseTag getTag() {
        return tag;
    }

    /**
     * @return an id that points to some object that 
     * is *not* of type basename
     */
    protected String getIDForWrongType() {
        return "Helena"; // <- points to a topic, not to a basename
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tag = new UseBasenameTag();
        super.setUp();
    }

    // test resolving the basename by id
    public void testResolveByID() throws Exception {

        String id = "name_of_maia";
        super.checkResolveByID(id);
        // assert that the object returned is 
        // a basename and that it is the one expected
        assertEquals(id, tag.getBaseName().getID());
        
    }

    // test resolving the basename by id
    public void testResolveByIDInExplicitMap() throws Exception {

        String id2 = "test2-bn2";
        super.checkResolveByIDInMap(id2,tm2);
        // assert that the object returned is 
        // a basename and that it is the one expected
        assertEquals(id2, tag.getBaseName().getID());
        
    }

    // test that a basename can be retrieved by sourceLocator
    public void testResolveBySourceLocator() throws Exception {

        String id = "name_of_elektra";
        String adress = baseLoc + "#" + id;
        tag.setSourceLocator(adress);

        // executes doTag(...) and asserts that
        // the body of the tag gets called.
        super.checkSuccessfullDoTagExecution();

        // assert that the
        // basename returned is the one expected
        BaseName bn = tag.getBaseName();
        assertEquals(id, bn.getID());

    }



    // test the case that the resolvement fails
    // because of an unknown id
    public void testNonExistanceID() throws Exception {

        // check that resolvement fails
        super.checkResolveNonExistanceID();

        // basename should be null
        assertNull(tag.getBaseName());

    }

    // test the case that the resolvement fails
    // because of an unknown sourceLocator
    public void testNonExistantSourceLocator() throws Exception {

        // check resolvement for a sourceLocator
        // that is not part of the map
        super.checkNonExistantSourceLocator();
        
        // basename should be null
        assertNull(tag.getBaseName());

    }



    // test setting a basename directly
    public void testSetBasename() 
        throws Exception
    {
        
        String id = "name_of_maia";
        BaseName basename = (BaseName) tm.getObjectByID(id);

        // sets association directly
        tag.setBaseName(basename);
        
        assertEquals(basename, tag.getBaseName());

    }

    // test resolvement failure
    // with mode ADD
    public void testNonExistanceIDWithADD() throws Exception {

        String id = "basename_does_not_exist";
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

        // set a name
        String name = "Helen of Troy";
        tag.setName(name);
        
        Topic helena = utt.getTopic();
        int co = helena.getNames().size();

        // resolving
        // this should lead to the creation
        // of a new basename in topic Helena
        tag.doTag(null);
        assertEquals(co + 1, helena.getNames().size());

        BaseName bn = tag.getBaseName();
        assertEquals(bn.getParent(), helena);

        // assert the name
        assertEquals(name, bn.getData());
    }
}