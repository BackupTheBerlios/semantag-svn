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



    // test setting an association directly
    public void testSetAssociation() 
        throws Exception
    {
        
        String id = "as00003";
        Association assoc = (Association) tm.getObjectByID(id);

        // sets association directly
        tag.setAssociation(assoc);
        
        assertEquals(assoc, tag.getAssociation());

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
//    public void testNonExistantVariable() throws Exception {
//
//        // check resolvement for a variable
//        // that was not set in the context
//        super.checkNonExistantVariable();
//
//        // association should be null
//        assertNull(tag.getAssociation());
//
//    }

    // test resolvement failure
    // with mode ADD
    public void testNonExistanceIDWithADD() throws Exception {

        String id = "does_not_exist";
        tag.setId(id);
        tag.setNonexistant(BaseUseTag.NE_ADD);
        setScriptForTagBody(tag);

        int co = tm.getAssociations().size();

        // resolving
        // this should lead to the creation
        // of a new association in topic Helena
        tag.doTag(null);
        assertEquals(co + 1, tm.getAssociations().size());

        Association bn = tag.getAssociation();
        assertEquals(bn.getParent(), tm);

    }
    
    // tests that a resolved item is bound to 
    // the variable specified by the var-property
    // tests secondly that a variable is reset to 
    // null, if the item could not be found
    // by a succeding tag
    public void testBoundToVariable()
        throws Exception
    {
        String varname ="ITEM";
        String id = "as00003";
        Association assoc = (Association) tm.getObjectByID(id);
        // resolver shall resolve by id
        tag.setAssociation(assoc);

        // resolved topic shall be stored in
        // the variable named TOPIC
        tag.setVar(varname);
        
        // resolve
        setScriptForTagBody(tag);
        tag.doTag(null);
        
        // the item should be stored in the variable
        assertEquals(assoc, ctx.getVariable(varname));
        
        // make a new tag, bound to the same variable 
        // set resolvement to non existant topic
        tag = new UseAssociationTag();
        tag.setContext(ctx);
        tag.setVar(varname);
        tag.setId("nonexistantntifd");

        // resolve
        tag.doTag(null);

        // the variable should be resetted
        assertNull(ctx.getVariable(varname));
        
    }
    
    // test that the body of the tag is skipped
    // when tag is in non-existant-mode "add"
    // and the topicmap object exists already
    public void testADDForExistant() 
        throws Exception
    {
        String id = "as00003";
        Association assoc = (Association) tm.getObjectByID(id);

        tag.setNonexistant("add");
        tag.setAssociation(assoc);
        
        // set a script for the body of the tag
        scriptWasCalled = false;
        setScriptForTagBody(tag);
        
        int co = tm.getAssociations().size();
        tag.doTag(null);
        // there should be the same count
        assertEquals(co, tm.getAssociations().size());
        
        // the body script must not have been called
        assertFalse(scriptWasCalled);
    }


}