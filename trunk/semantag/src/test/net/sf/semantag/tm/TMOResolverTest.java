package net.sf.semantag.tm;
import net.sf.semantag.TestData;

import org.apache.commons.jelly.JellyContext;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;


/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class TMOResolverTest extends TMTagTestBase {

    private final static String baseLoc = TestData.TM_GREEKS_BASELOCATOR;

    TopicMap tm;
    TMOResolver resolver;

   

    // stores a topic map object in the context and uses
    // the TMOResolver to retrieve it by key name
    public void testIdentifyByVariable() 
        throws Exception
    {
        String id = "as00001";
        String varName = "TMO";

        TopicMapObject tmo = tm.getObjectByID(id);
        assertNotNull(tmo);
        JellyContext ctx = new JellyContext();
        ctx.setVariable(varName, tmo);
        
        resolver.setVariable(varName);
        assertEquals(tmo, resolver.getTopicMapObject(tm, ctx));
        
    }

    // sets the id and tests whether the
    // TMOResolver returns the expected topic map object
    public void testIdentifyById() 
        throws Exception
    {
        String id = "name_of_maia";
        resolver.setId(id);
        TopicMapObject tmo = resolver.getTopicMapObject(tm, new JellyContext());
        String adress = ((Locator)tmo.getSourceLocators().iterator().next()).getAddress();
        assertEquals(baseLoc+"#"+id,adress);
    }

    // sets the sourceLocator and tests whether the
    // TMOResolver returns the expected topic map object
    public void testIdentifyBySourceLocator() 
        throws Exception
    {
        String id = "name_of_maia";
        String adress = baseLoc+"#"+id; 
        resolver.setSourceLocator(adress);

        TopicMapObject tmo = resolver.getTopicMapObject(tm, new JellyContext());
        assertEquals(id,tmo.getID());
    }



    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_GREEKS_XTM, baseLoc);
        resolver = new TMOResolver();

    }
}
