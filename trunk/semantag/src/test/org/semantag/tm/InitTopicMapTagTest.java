package org.semantag.tm;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.MissingAttributeException;
import org.semantag.tm.Dictionary;
import org.semantag.tm.InitTopicMapTag;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 07.09.2004
 */
public class InitTopicMapTagTest extends TMTagTestBase {
   
    // doTag(XMLOutput) should fail, if no baseLocator was set
    public void testValidateFails() throws Exception {
        InitTopicMapTag tag = new InitTopicMapTag();

        try {
            tag.doTag(null);
            fail("Expected Missing attribute exception");
        } catch (MissingAttributeException ex) {
            // expected
        }
    }

    // inits a topic map
    public void testInitTopicMap() throws Exception {
        
        InitTopicMapTag tag = new InitTopicMapTag();
        tag.setContext(new JellyContext());
        setScriptForTagBody(tag);
        
        tag.setBaselocator("http://base.loc");
        tag.doTag(null);

        // check that the map is accessible
        TopicMap tm = tag.getTopicMapFromContext(null);
        assertNotNull(tm);

        //check that it is accessible via getTopicMap() too
        assertEquals(tm, tag.getTopicMap());
        
        // check that the tm is empty
        assertEquals(0, tm.getTopicCount());
        
        // check whether the body was processed
        assertTrue(scriptWasCalled);
    }

    
    // tests that specifying a variable leads to
    // the topicmap being accessible through this
    // variable
    public void testTMStoredInVariable()
        throws Exception
    {
        InitTopicMapTag tag = new InitTopicMapTag();
        tag.setContext(new JellyContext());
        setScriptForTagBody(tag);
        
        tag.setBaselocator("http://base.loc");
        tag.setVar("TOPICMAP");
        tag.doTag(null);

        // check that the map i stored in JellyContext
        assertEquals(tag.getTopicMap(), tag.getContext().getVariable("TOPICMAP"));
        
        // check that the map is accessible
        TopicMap tm = tag.getTopicMapFromContext("TOPICMAP");
        assertNotNull(tm);
        
        
    }

    // tests that the first map opened becomes
    // and remains the default topic map
    public void testDefaultTopicMap()
        throws Exception
    {
        JellyContext ctx = new JellyContext();
        
        InitTopicMapTag tag = new InitTopicMapTag();
        tag.setContext(ctx);
        setScriptForTagBody(tag);
        
        tag.setBaselocator("http://base.loc");
        tag.doTag(null);
       
        // check that the map is the default one
        TopicMap tm = tag.getTopicMapFromContext(null);
        assertNotNull(tm);
        
        // init second map
        InitTopicMapTag tag2 = new InitTopicMapTag();
        tag2.setContext(ctx);
        setScriptForTagBody(tag2);
        tag2.setVar("TM2");
        tag2.setBaselocator("http://base2.loc");
        tag2.doTag(null);

        // check that the map is the default one
        TopicMap tm2 = tag2.getTopicMap();
        assertEquals("http://base2.loc",tm2.getBaseLocator().getAddress());
        
        // assert that tm is still the default
        assertEquals(tm, tag2.getContext().getVariable(Dictionary.KEY_TOPICMAP));

        
        //access tm2 through variable
        assertEquals(tm2, tag2.getTopicMapFromContext("TM2"));
        
        
        
    }
 
}