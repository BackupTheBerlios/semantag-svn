package net.sf.semantag.tm;


import org.tm4j.topicmap.Association;

/**
 * 
 * @author cf
 * @version 0.1, created on 09.09.2004
 */
public class UseAssociationTagTest extends UseTagTestBase {
    private UseAssociationTag tag;

    
    
    /**
     * @throws Exception
     */
    public UseAssociationTagTest() throws Exception {
        super();
    }
    /**
     * @param name
     * @throws Exception
     */
    public UseAssociationTagTest(String name) throws Exception {
        super(name);
    }

    protected BaseUseTag getTag() {
        return tag;
    }

    /**
     * @return an id that points to some object that 
     * is *not* of type association
     */
    protected String getIDForWrongType() {
        return "Helena"; // <- points to a topic, not to a association
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tag = new UseAssociationTag();
        super.setUp();
    }

    // test resolving the association by id
    public void testResolveByID() throws Exception {

        String id = "as00001";
        super.checkResolveByID(id);
        // assert that the object returned is 
        // a association and that it is the one expected
        assertEquals(id, tag.getAssociation().getID());
        
    }

    // test resolving the association by id
    public void testResolveByIDInExplicitMap() throws Exception {

        String id2 = "test2-as-01";
        super.checkResolveByIDInMap(id2,tm2);
        // assert that the object returned is 
        // a association and that it is the one expected
        assertEquals(id2, tag.getAssociation().getID());
        
    }

    // test that a association can be retrieved by sourceLocator
    public void testResolveBySourceLocator() throws Exception {

        String id = "as00002";
        String adress = baseLoc + "#" + id;
        tag.setSourceLocator(adress);

        // executes doTag(...) and asserts that
        // the body of the tag gets called.
        super.checkSuccessfullDoTagExecution();

        // assert that the
        // association returned is the one expected
        Association bn = tag.getAssociation();
        assertEquals(id, bn.getID());

    }

    // test that a association that is stored in
    // the context can be retrieved by variablename
    public void testResolveByVariable() throws Exception {

        String id = "as00003";
        Association bn = (Association) tm.getObjectByID(id);

        // call common UseTag-test-code
        super.checkResolveByVariable(id, bn);

        assertEquals(bn, tag.getAssociation());
    }

    // test the case that the resolvement fails
    // because of an unknown id
    public void testNonExistanceID() throws Exception {

        // check that resolvement fails
        super.checkResolveNonExistanceID();

        // association should be null
        assertNull(tag.getAssociation());

    }

    // test the case that the resolvement fails
    // because of an unknown sourceLocator
    public void testNonExistantSourceLocator() throws Exception {

        // check resolvement for a sourceLocator
        // that is not part of the map
        super.checkNonExistantSourceLocator();
        
        // association should be null
        assertNull(tag.getAssociation());

    }

    
    // test the case that the resolvement fails
    // because of an unknown variable
    public void testNonExistantVariable() throws Exception {

        // check resolvement for a variable
        // that was not set in the context
        super.checkNonExistantVariable();

        // association should be null
        assertNull(tag.getAssociation());

    }

    // test resolvement failure
    // with mode ADD
    public void testNonExistanceIDWithADD() throws Exception {

        String id = "does_not_exist";
        tag.setId(id);
        tag.setNonexistant(BaseUseTag.NE_ADD);
        setScriptForTagBody(tag);

        // make the parent to which the association will be added
//        UseTopicTag utt = new UseTopicTag();
//        utt.setId("Helena");
//        utt.setContext(ctx);
//
//        // set the UseTopicTag as parent of the
//        // UseAssociationTag
//        tag.setParent(utt);

//        Topic helena = utt.getTopic();
        int co = tm.getAssociations().size();

        // resolving
        // this should lead to the creation
        // of a new association in topic Helena
        tag.doTag(null);
        assertEquals(co + 1, tm.getAssociations().size());

        Association bn = tag.getAssociation();
        assertEquals(bn.getParent(), tm);

    }
}