package net.sf.semantag.tm;

import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;

/**
 * 
 * @author cf
 * @version 0.1, created on 09.09.2004
 */
public class UseOccurrenceTagTest extends UseTagTestBase {
    private UseOccurrenceTag tag;

    
    
    /**
     * @throws Exception
     */
    public UseOccurrenceTagTest() throws Exception {
        super();
    }
    /**
     * @param name
     * @throws Exception
     */
    public UseOccurrenceTagTest(String name) throws Exception {
        super(name);
    }

    protected BaseUseTag getTag() {
        return tag;
    }

    /**
     * @return an id that points to some object that 
     * is *not* of type occurrence
     */
    protected String getIDForWrongType() {
        return "Helena"; // <- points to a topic, not to a occurrence
    }

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tag = new UseOccurrenceTag();
        super.setUp();
    }

    // test resolving the occurrence by id
    public void testResolveByID() throws Exception {

        String id = "occ_pollux";
        super.checkResolveByID(id);
        // assert that the object returned is 
        // a occurrence and that it is the one expected
        assertEquals(id, tag.getOccurrence().getID());
        
    }

    // test resolving the occurrence by id
    public void testResolveByIDInExplicitMap() throws Exception {

        String id2 = "test2-occ_2";
        super.checkResolveByIDInMap(id2,tm2);
        // assert that the object returned is 
        // a occurrence and that it is the one expected
        assertEquals(id2, tag.getOccurrence().getID());
        
    }

    // test that a occurrence can be retrieved by sourceLocator
    public void testResolveBySourceLocator() throws Exception {

        String id = "occ_hermes1";
        String adress = baseLoc + "#" + id;
        tag.setSourceLocator(adress);

        // executes doTag(...) and asserts that
        // the body of the tag gets called.
        super.checkSuccessfullDoTagExecution();

        // assert that the
        // occurrence returned is the one expected
        Occurrence bn = tag.getOccurrence();
        assertEquals(id, bn.getID());

    }

    // test that a occurrence that is stored in
    // the context can be retrieved by variablename
    public void testResolveByVariable() throws Exception {

        String id = "occ_hermes2";
        Occurrence bn = (Occurrence) tm.getObjectByID(id);

        // call common UseTag-test-code
        super.checkResolveByVariable(id, bn);

        assertEquals(bn, tag.getOccurrence());
    }

    // test the case that the resolvement fails
    // because of an unknown id
    public void testNonExistanceID() throws Exception {

        // check that resolvement fails
        super.checkResolveNonExistanceID();

        // occurrence should be null
        assertNull(tag.getOccurrence());

    }

    // test the case that the resolvement fails
    // because of an unknown sourceLocator
    public void testNonExistantSourceLocator() throws Exception {

        // check resolvement for a sourceLocator
        // that is not part of the map
        super.checkNonExistantSourceLocator();
        
        // occurrence should be null
        assertNull(tag.getOccurrence());

    }

    
    // test the case that the resolvement fails
    // because of an unknown variable
    public void testNonExistantVariable() throws Exception {

        // check resolvement for a variable
        // that was not set in the context
        super.checkNonExistantVariable();

        // occurrence should be null
        assertNull(tag.getOccurrence());

    }

    // test resolvement failure
    // with mode ADD
    public void testNonExistanceIDWithADD() throws Exception {

        String id = "does_not_exist";
        tag.setId(id);
        tag.setNonexistant(BaseUseTag.NE_ADD);
        setScriptForTagBody(tag);

        // make the parent to which the occurrence will be added
        UseTopicTag utt = new UseTopicTag();
        utt.setId("Helena");
        utt.setContext(ctx);

        // set the UseTopicTag as parent of the
        // UseOccurrenceTag
        tag.setParent(utt);

        Topic helena = utt.getTopic();
        int co = helena.getOccurrences().size();

        // resolving
        // this should lead to the creation
        // of a new occurrence in topic Helena
        tag.doTag(null);
        assertEquals(co + 1, helena.getOccurrences().size());

        Occurrence bn = tag.getOccurrence();
        assertEquals(bn.getParent(), helena);

    }
}