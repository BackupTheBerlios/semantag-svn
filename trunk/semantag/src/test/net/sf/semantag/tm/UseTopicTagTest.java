package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyContext;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

import net.sf.semantag.TestData;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class UseTopicTagTest extends BaseTMTagTest {
    
    private TopicMap tm;
    private UseTopicTag utt;
    private JellyContext ctx;
    
    // an often used topic
    private Topic john; 
    
    public void testSetTopicVar() 
        throws Exception
    {
        
        String topicvar="TOPIC";
        
        // store topic and topicmap in context
        ctx.setVariable(topicvar, john);
        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);

        // resolver shall resolve by variable
        utt.setTopicVar(topicvar);
        
        assertEquals(john, utt.getTopic());
    }

    public void testSetTopicID() {
    }

    public void testSetTopicName() {
    }

    public void testSetTopicSourceLocator() {
    }

    public void testSetTopicSubject() {
    }

    public void testSetTopicSubjectIndicator() {
    }

    public void testSetTmVar() {
    }

    public void testShallFailOnNonexistant() {
    }

    public void testShallAddOnNonexistant() {
    }

    public void testShallIgnoreOnNonexistant() {
    }

    public void testSetId() {
    }

    public void testSetSourceLocator() {
    }
    
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM, TestData.TM_JOHN_BASELOCATOR);

        utt = new UseTopicTag();
        ctx = new JellyContext();
        utt.setContext(ctx);
        
        john = tm.getTopicByID("john");
    
    }


}
