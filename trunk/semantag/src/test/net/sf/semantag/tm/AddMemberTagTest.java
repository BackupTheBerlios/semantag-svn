package net.sf.semantag.tm;

import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class AddMemberTagTest extends TMTagTestBase {

    private TopicMap tm;

    private AddMemberTag amt;

    private JellyContext ctx;

    // often used topics
    private Association testAssoc;
    private Topic father;
    private Topic leda;

    // an existing id 
    final String personID  = "sparta";
    
    // used to generate unique ids for every  
    // basename added.
    private static int addCount = 0;

    
    // test adding a member to an association that is 
    // defined by a parent ContextAssociation
    public void testAddMemberToImplicitTopic() throws Exception {

        doAddMember(null, null, father, leda ,testAssoc, false);
        
    }
    
    // tests that specifying an association explicitly via
    // the association attribute works
    public void testAddOccurenceToExplicitTopic() throws Exception {

        doAddMember(null, null, father, leda,testAssoc, true);
    }

    
    // tests that specifying an association explicitly via
    // the association attribute has priority before a parent tag
    public void testImplicitAndExplicitTopic() throws Exception {

        // set the parent-tag
        // that shall remain unaltered in this test
        UseAssociationTag utt = new UseAssociationTag();
        utt.setAssociation(testAssoc);
        amt.setParent(utt);
        
        // store member count of assoc
        int co = testAssoc.getMembers().size();
        
        // specify assoc explicitly
        Association assoc2 = (Association)tm.getObjectByID("as00002");
        
        // add the occurrence to nina
        doAddMember(null, null, father, leda, assoc2, true);
        
        // assert that john remained unaltered
        assertEquals(co, testAssoc.getMembers().size());
        
        
    }
    
    // tests several combinations of passing roles and players
    public void testDataAndResource() throws Exception {

        // pass roleSpec
        doAddMember(null, null, father, null, testAssoc, true);

        // pass Player
        doAddMember(null, null, null, leda,testAssoc, false);

        // pass both
        doAddMember(null, null, father, leda,testAssoc, false);

        // pass neither
        doAddMember(null, null, null, null, testAssoc, false);
    }
    
    
    
    // tests the tag outside of the context of a topic
    public void testMissingParent() throws Exception {


        try{
            doAddMember(null, null, null, null ,null, false);
        }
        catch(JellyTagException e){
            String expected = "AddMember must be either the children of an object";
            assertTrue(e.getReason().startsWith(expected));
        }
    }
    
    public void testAddWithExistingID() throws Exception {
        
        
        // store member count before
        int co = testAssoc.getMembers().size();

        // add member with an existing id
        try {
            doAddMember(personID,null,null, null, testAssoc, true);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
            assertEquals(e.getCause().getClass(), DuplicateObjectIDException.class);
        }

        // assert that no new member was created
        assertEquals(co, testAssoc.getMembers().size());

    }
    

    public void testAddWithExistingSourceLocator() throws Exception {

        // any existing sourceLocator
        Locator sl = (Locator) tm.getTopicByID(personID).getSourceLocators().iterator().next();

        // store member count before
        int co = testAssoc.getMembers().size();

        // add member
        try {
            doAddMember(null ,sl.getAddress(), null, null, testAssoc, true);
            fail("Expected Exception");
        } catch (JellyTagException e) {
            // expected
            assertTrue(e.getReason().startsWith("The topicmap already contains a sourceLocator"));
        }

        // assert that no new member was created
        assertEquals(co, testAssoc.getMembers().size());

    }
    
    
    /**
     * Adds a basename with the given attributes.
     * Tests various properties
     * @param id
     * @param sl
     * @param role
     * @param parent a topic that is expected to be the parent of 
     * the basename
     * @throws Exception
     */
    private void doAddMember(String id, String sl, Topic role, Topic player, 
            Association parent, boolean parentIsExplicit)
        throws Exception
    {

        addCount++;
        
        if(id == null) id = "mem_nonexistant_id"+addCount;
        if(sl == null) sl = "http://non.existant.org/occ/"+addCount;
        
        String varName = "OCC";
        
        // set id and sourcelocator
        amt.setId(id);
        amt.setSourceLocator(sl);

        // set role and player
        amt.setRole(role);
        amt.setPlayer(player);
        
        // set the parent
        if(parent != null){
            if(parentIsExplicit){
                amt.setAssociation(parent);
            }
            else {
                // ... implicit as tag
                UseAssociationTag utt = new UseAssociationTag();
                utt.setAssociation(parent);
                amt.setParent(utt);
            }
        }
        
        // set the name of the variable
        // that shall store the member
        amt.setVar(varName);

        // add a dummy body
        setScriptForTagBody(amt);

        // store member count before
        int co = 0;
        if(parent != null) co = parent.getMembers().size();
        
        // add member
        amt.doTag(null);

        // assert that a new member was created
        assertEquals(co + 1, parent.getMembers().size());

        // assert that there is a member with the
        // id and the sourceLocator specified
        Member bn = (Member)tm.getObjectByID(id);
        assertEquals(sl, ((Locator) bn.getSourceLocators().iterator().next())
                .getAddress());

        // assert role and player
        assertEquals(role, bn.getRoleSpec());
        if(player != null) assertEquals(player, bn.getPlayers().iterator().next());
        else assertEquals(0, bn.getPlayers().size());
        
        // assert that the member is retrievable
        // via the ContextOccurrence-API
        assertEquals(bn, amt.getMember());
        
        // assert that the parent is the one expected
        assertEquals(parent, bn.getParent());
        
        // assert that the member is retrievable
        // via the context variable
        assertEquals(bn, ctx.getVariable(varName));
        
        // assert that the body was called
        assertTrue(scriptWasCalled);

    }

 


    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_GREEKS_XTM,
                TestData.TM_GREEKS_BASELOCATOR);

        amt = new AddMemberTag();
        ctx = new JellyContext();
        amt.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        testAssoc = (Association)tm.getObjectByID("as00001");
        father = tm.getTopicByID("role_Vater");
        leda = tm.getTopicByID("Leda");


    }

}