package org.semantag.tm;

import java.util.ArrayList;
import java.util.Collection;


import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.TagSupport;
import org.semantag.TestData;
import org.semantag.tm.AddInstanceOfTag;
import org.semantag.tm.Dictionary;
import org.semantag.tm.UseAssociationTag;
import org.semantag.tm.UseOccurrenceTag;
import org.semantag.tm.UseTopicTag;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class AddInstanceOfTagTest extends TMTagTestBase {

    private TopicMap tm;

    private AddInstanceOfTag iot;

    private JellyContext ctx;


    
    // tests to set the type of an Association
    public void testAddInstanceOfToAssociation() throws Exception {

        Association assoc = (Association)tm.getObjectByID("assoc_without_type");
        Topic type = tm.getTopicByID("test2-assoctype2"); 

        // assert preconditions
        assertTrue(!assoc.isOfType(type));
        
        // specify assoc explicitly
        doAddInstanceOf(type, assoc, null,assoc);
        
        // remove type for next test
        assoc.setType(null);
        assertTrue(!assoc.isOfType(type));

        // ----------------------------------
        // specify assoc implicitly
        UseAssociationTag uat = new UseAssociationTag();
        uat.setAssociation(assoc);
        
        // test
        doAddInstanceOf(type, null, uat,assoc);

        // remove type for next test
        assoc.setType(null);
        assertTrue(!assoc.isOfType(type));
        
        // ----------------------------------
        // assert that explicitly specified associations 
        // have a higher proprity than those specified 
        // implicitly
        Association assoc2 = (Association)tm.getObjectByID("test2-as-01");
        Topic typeOfAssoc2 = assoc2.getType();
        
        // type should be different in order to
        // have meaningful test results
        assertFalse(type.equals(typeOfAssoc2));
        
        uat = new UseAssociationTag();
        uat.setAssociation(assoc2);
        
        // test
        doAddInstanceOf(type, assoc, uat,assoc);
        
        // assoc2 should be unalterd
        assertEquals(typeOfAssoc2, assoc2.getType());
    }
    

    // tests to set the type of an Occurrence
    public void testAddInstanceOfToOccurrence() throws Exception {

        Occurrence occ = (Occurrence)tm.getObjectByID("occ_without_type");
        Occurrence occ2 = (Occurrence)tm.getObjectByID("occ_with_type");
        Topic type = tm.getTopicByID("test2-occtype"); 
        Topic type2 = occ2.getType();

        // assert preconditions
        assertTrue(!occ.isOfType(type));
        
        // ----------------------------------
        // specify occurrence explicitly
        doAddInstanceOf(type, occ, null,occ);
        
        // remove type for next test
        occ.setType(null);
        assertTrue(!occ.isOfType(type));

        
        
        // ----------------------------------
        // specify occurrence implicitly
        UseOccurrenceTag uat = new UseOccurrenceTag();
        uat.setOccurrence(occ);
        
        // test
        doAddInstanceOf(type, null, uat,occ);

        // remove type for next test
        occ.setType(null);
        assertTrue(!occ.isOfType(type));
        
        // ----------------------------------
        // assert that explicitly specified occurrences 
        // have a higher proprity than those specified 
        // implicitly
        
        // type should be different in order to
        // have meaningful test results
        assertFalse(type.equals(type2));
        
        uat = new UseOccurrenceTag();
        uat.setOccurrence(occ2);
        
        // test
        doAddInstanceOf(type, occ, uat,occ);
        
        // occ2 should be unaltered
        assertEquals(type2, occ2.getType());
    }
    
    // tests to set the type of a Topic
    public void testAddInstanceOfToTopic() throws Exception {

        Topic topic = tm.getTopicByID("topic_without_type");
        Topic topic2 = tm.getTopicByID("test2-t2");
        Topic type = tm.getTopicByID("test2-topictype"); 
        Topic type2 = (Topic)topic2.getTypes().iterator().next();

        // assert preconditions
        assertTrue(!topic.isOfType(type));
        
        // ----------------------------------
        // specify topic explicitly
        doAddInstanceOf(type, topic, null,topic);
        
        // remove type for next test
        Collection t = new ArrayList(topic.getTypes());
        t.remove(type);
        topic.setTypes((Topic[])t.toArray(new Topic[t.size()]));
        assertTrue(!topic.isOfType(type));

        
        
        // ----------------------------------
        // specify topic implicitly
        UseTopicTag uat = new UseTopicTag();
        uat.setTopic(topic);
        
        // test
        doAddInstanceOf(type, null, uat,topic);

        // remove type for next test
        t = new ArrayList(topic.getTypes());
        t.remove(type);
        topic.setTypes((Topic[])t.toArray(new Topic[t.size()]));
        assertTrue(!topic.isOfType(type));
        
        // ----------------------------------
        // assert that explicitly specified occurrences 
        // have a higher proprity than those specified 
        // implicitly
        
        // type should be different in order to
        // have meaningful test results
        assertFalse(type.equals(type2));
        
        int co = topic2.getTypes().size();
        
        uat = new UseTopicTag();
        uat.setTopic(topic2);
        
        // test
        doAddInstanceOf(type, topic, uat,topic);
        
        // occ2 should be unaltered
        assertEquals(co, topic2.getTypes().size());
    }
    
    
    
    
    // tests to assert that the context is resolved
    // step by step from the current tag towards the root tag
    public void testResolveContextLinear() throws Exception {

        Occurrence occ = (Occurrence)tm.getObjectByID("occ_without_type");

        Topic topic = tm.getTopicByID("topic_without_type");
        Topic type = tm.getTopicByID("test2-topictype"); 

        // assert preconditions
        assertTrue(!occ.isOfType(type));
        assertTrue(!topic.isOfType(type));
        

        
        
        // ----------------------------------
        // specify a nested topic/occ -structure implicitly
        UseTopicTag uat = new UseTopicTag();
        uat.setTopic(topic);
        
        UseOccurrenceTag uot = new UseOccurrenceTag();
        uot.setOccurrence(occ);
        uot.setParent(uat);
        
        // test
        doAddInstanceOf(type, null, uot,occ);

    }
    /**
     * Adds a type to either the explicit instance or
     * to the implicit instance.
     * 
     * @param type
     * @param explInstance
     * @param implInstance
     * @param cmpInstance instance that is expected to be altered,
     * either via explicit or via implicit referencing
     * @throws Exception
     */
    private void doAddInstanceOf(Topic type, TopicMapObject explInstance, 
            TagSupport implInstance, TopicMapObject cmpInstance)
        throws Exception
    {

        
        // set type
        iot.setType(type);

        
        // set the instance both explicit and implicit
        if(explInstance != null) iot.setInstance(explInstance);
        if(implInstance != null) iot.setParent(implInstance);

        
        // add a dummy body
        setScriptForTagBody(iot);

        // add instance of
        iot.doTag(null);

        // assert that the instance is resolved to 
        // the expected object
        assertEquals(cmpInstance, iot.getInstance());

        if(cmpInstance instanceof Topic){
            assertTrue(((Topic)cmpInstance).isOfType(type));
        }
        else if(cmpInstance instanceof Association){
            assertTrue(((Association)cmpInstance).isOfType(type));
        }
        else if(cmpInstance instanceof Occurrence){
            assertTrue(((Occurrence)cmpInstance).isOfType(type));
        }
        
        // assert that the body was not called
        assertFalse(scriptWasCalled);

    }

 


    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_TEST2_XTM,
                "some.org/");

        iot = new AddInstanceOfTag();
        ctx = new JellyContext();
        iot.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);


    }

}