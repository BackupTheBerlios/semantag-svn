package net.sf.semantag.tm;


import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;

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

    


    // test setting a member directly
    public void testSetMember() 
        throws Exception
    {
        
        String id = "mem1";
        Member member = (Member) tm.getObjectByID(id);

        // sets member directly
        tag.setMember(member);
        
        assertEquals(member, tag.getMember());

    }

    // test resolvement failure
    // with mode ADD
    public void testNonExistanceIDWithADD() throws Exception {

        String id = "member_does_not_exist";
        tag.setId(id);
        tag.setNonexistant(BaseUseTag.NE_ADD);
        setScriptForTagBody(tag);

        // make the parent to which the member will be added
        UseAssociationTag utt = new UseAssociationTag();
        utt.setId("as00001");
        utt.setContext(ctx);

        // set the UseAssociationTag as parent of the
        // UseMemberTag
        tag.setParent(utt);

        // store member count of parent
        Association assoc = utt.getAssociation();
        int co = assoc.getMembers().size();

        // set role and a player
        Topic father = tm.getTopicByID("role_Vater");
        Topic leda = tm.getTopicByID("Leda");
        tag.setRole(father);
        tag.setPlayer(leda);
        
        
        // resolving
        // this should lead to the creation
        // of a new member in topic Helena
        tag.doTag(null);
        assertEquals(co + 1, assoc.getMembers().size());

        Member bn = tag.getMember();
        assertEquals(bn.getParent(), assoc);
        
        // assert role and player
        assertEquals(father, bn.getRoleSpec());
        assertEquals(leda, bn.getPlayers().iterator().next());

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
        String id = "mem3";
        Member member = (Member) tm.getObjectByID(id);
        // resolver shall resolve by id
        tag.setMember(member);

        // resolved topic shall be stored in
        // the variable named TOPIC
        tag.setVar(varname);
        
        // resolve
        setScriptForTagBody(tag);
        tag.doTag(null);
        
        // the item should be stored in the variable
        assertEquals(member, ctx.getVariable(varname));
        
        // make a new tag, bound to the same variable 
        // set resolvement to non existant topic
        tag = new UseMemberTag();
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
        String id = "mem3";
        Member member = (Member) tm.getObjectByID(id);

        tag.setNonexistant("add");
        tag.setMember(member);
        
        // set a script for the body of the tag
        scriptWasCalled = false;
        setScriptForTagBody(tag);
        
        tag.doTag(null);
        
        // the body script must not have been called
        assertFalse(scriptWasCalled);
    }

}