package net.sf.semantag.tm;
import org.apache.commons.jelly.JellyContext;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

import net.sf.semantag.TestData;
import net.sf.semantag.tm.TopicResolver;


/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class TopicResolverTest extends BaseTMTagTest {

    TopicMap tm;
    TopicResolver tr;

   

    // stores a topic in the context and uses
    // the TopicResolver to retrieve it by key name
    public void testIdentifyTopicByVariable() 
        throws Exception
    {
        String id = "john";
        String varName = "TOPIC";

        Topic t = tm.getTopicByID(id);
        JellyContext ctx = new JellyContext();
        ctx.setVariable(varName, t);
        
        tr.setTopicVar(varName);
        assertEquals(t, tr.getTopic(tm, ctx));
        
    }

    // sets the sourceLocator and tests whether the
    // TopicRelover returns the expected topic
    public void testIdentifyTopicByID() 
        throws Exception
    {
        tr.setTopicID("john");
        Topic t = tr.getTopic(tm, new JellyContext());
        String adress = ((Locator)t.getSourceLocators().iterator().next()).getAddress();
        assertEquals(TestData.TM_JOHN_BASELOCATOR+"#john",adress);
    }

    // sets the sourceLocator and tests whether the
    // TopicRelover returns the expected topic
    public void testIdentifyTopicBySourceLocator() 
        throws Exception
    {
        tr.setTopicSourceLocator(TestData.TM_JOHN_BASELOCATOR+"#john");

        Topic t = tr.getTopic(tm, new JellyContext());
        assertEquals("john",t.getID());
    }

    // sets the subject and tests whether the
    // TopicResolver returns the expected topic
    public void testIdentifyTopicBySubject() 
        throws Exception
    {
        tr.setTopicSubject("http://www.about_john.org");

        Topic t = tr.getTopic(tm, new JellyContext());
        assertEquals("johns_site",t.getID());
    }


    // sets the subjectIndicator-property and tests whether 
    // the TopicResolver returns the expected topic
    public void testIdentifyTopicBySubjectIndicator() 
        throws Exception
    {
        tr.setTopicSubjectIndicator("http://www.fiatpanda.it/");

        Topic t = tr.getTopic(tm, new JellyContext());
        assertEquals("fiat-panda",t.getID());
    }

    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_JOHN_LTM, TestData.TM_JOHN_BASELOCATOR);
        tr = new TopicResolver();

    }
}
