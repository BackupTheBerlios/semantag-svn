package net.sf.semantag.tm;


import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Member;

/**
 * 
 * @author cf
 * @version 0.1, created on 09.09.2004
 */
public class UseMemberTagTest extends UseTagTestBase {
    
    private UseMemberTag tag;

    
    
    /**
     * @throws Exception
     */
    public UseMemberTagTest() throws Exception {
        super();
    }
    /**
     * @param name
     * @throws Exception
     */
    public UseMemberTagTest(String name) throws Exception {
        super(name);
    }

    protected BaseUseTag getTag() {
        return tag;
    }

    /**
     * @return an id that points to some object that 
     * is *not* of type member
     */
    protected String getIDForWrongType() {
        return "Helena"; // <- points to a topic, not to a member
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tag = new UseMemberTag();
        super.setUp();
    }

    // test resolving the member by id
    public void testResolveByID() throws Exception {

        String id = "mem1";
        super.checkResolveByID(id);
        // assert that the object returned is 
        // a member and that it is the one expected
        assertEquals(id, tag.getMember().getID());
        
    }

    // test resolving the member by id
    public void testResolveByIDInExplicitMap() throws Exception {

        String id2 = "test2-mem2";
        super.checkResolveByIDInMap(id2,tm2);
        // assert that the object returned is 
        // a member and that it is the one expected
        assertEquals(id2, tag.getMember().getID());
        
    }

    // test that a member can be retrieved by sourceLocator
    public void testResolveBySourceLocator() throws Exception {

        String id = "mem3";
        String adress = baseLoc + "#" + id;
        tag.setSourceLocator(adress);

        // executes doTag(...) and asserts that
        // the body of the tag gets called.
        super.checkSuccessfullDoTagExecution();

        // assert that the
        // member returned is the one expected
        Member bn = tag.getMember();
        assertEquals(id, bn.getID());

    }

    // test that a member that is stored in
    // the context can be retrieved by variablename
    public void testResolveByVariable() throws Exception {

        String id = "mem1";
        Member bn = (Member) tm.getObjectByID(id);

        // call common UseTag-test-code
        super.checkResolveByVariable(id, bn);

        assertEquals(bn, tag.getMember());
    }

    // test the case that the resolvement fails
    // because of an unknown id
    public void testNonExistanceID() throws Exception {

        // check that resolvement fails
        super.checkResolveNonExistanceID();

        // member should be null
        assertNull(tag.getMember());

    }

    // test the case that the resolvement fails
    // because of an unknown sourceLocator
    public void testNonExistantSourceLocator() throws Exception {

        // check resolvement for a sourceLocator
        // that is not part of the map
        super.checkNonExistantSourceLocator();
        
        // member should be null
        assertNull(tag.getMember());

    }

    
    // test the case that the resolvement fails
    // because of an unknown variable
    public void testNonExistantVariable() throws Exception {

        // check resolvement for a variable
        // that was not set in the context
        super.checkNonExistantVariable();

        // member should be null
        assertNull(tag.getMember());

    }

    // test resolvement failure
    // with mode ADD
    public void testNonExistanceIDWithADD() throws Exception {

        String id = "does_not_exist";
        tag.setId(id);
        tag.setNonexistant(BaseUseTag.NE_ADD);
        setScriptForTagBody(tag);

        // make the parent to which the member will be added
        UseAssociationTag utt = new UseAssociationTag();
        utt.setId("as00001");
        utt.setContext(ctx);

        // set the UseTopicTag as parent of the
        // UseMemberTag
        tag.setParent(utt);

        Association assoc = utt.getAssociation();
        int co = assoc.getMembers().size();

        // resolving
        // this should lead to the creation
        // of a new member in topic Helena
        tag.doTag(null);
        assertEquals(co + 1, assoc.getMembers().size());

        Member bn = tag.getMember();
        assertEquals(bn.getParent(), assoc);

    }
}