package net.sf.semantag.tm;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderFactory;
import org.tm4j.topicmap.memory.TopicMapProviderFactoryImpl;
import org.tm4j.topicmap.source.SerializedTopicMapSource;
import org.tm4j.topicmap.source.TopicMapSource;
import org.tm4j.topicmap.utils.LTMBuilder;

import junit.framework.TestCase;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class TMTagTestBase extends TestCase {

    // flag, to indicate that the body of a
    // tag was called.
    protected boolean scriptWasCalled = false;

    /**
     * 
     */
    public TMTagTestBase() {
        super();
    }

    /**
     * @param name
     */
    public TMTagTestBase(String name) {
        super(name);
    }

    protected static TopicMap getTopicMapFromResource(String resource,
            String baseLocatorAdress) throws Exception {

        
        // fixture
        // loading topicmap
        TopicMapProviderFactory providerFactory = new TopicMapProviderFactoryImpl();
        TopicMapProvider pr;
        TopicMapSource topicmapSource;
        
        pr = providerFactory.newTopicMapProvider(new Properties());

        LocatorFactory locatorFactory = pr.getLocatorFactory();

        Locator baseLocator = locatorFactory.createLocator("URI",
                baseLocatorAdress);

        InputStream is = TMTagTestBase.class.getResourceAsStream(resource);

        int l = resource.length();
        if (l > 4 && resource.lastIndexOf(".ltm") == l - 4) {
            topicmapSource = new SerializedTopicMapSource(is,baseLocator, new LTMBuilder());
//          tm = pr.addTopicMap(is, baseLocator, null, new LTMBuilder());
        } else {
            topicmapSource = new SerializedTopicMapSource(is,baseLocator);
//          tm = pr.addTopicMap(is, baseLocator, null);
        }
        return pr.addTopicMap(topicmapSource);
    }
    
    
    /**
     * Sets the body of the given tag to a script.
     * The script sets the <code>scriptWasCalled</code>
     * field to true, by the time its run-method is invoked.
     * @param tag
     */
    protected void setScriptForTagBody(TagSupport tag){
        
        scriptWasCalled = false;

        tag.setBody(new Script() {
            public Script compile() throws JellyException {
                return null;
            }

            public void run(JellyContext arg0, XMLOutput arg1)
                    throws JellyTagException {
                scriptWasCalled = true;
            }
        });

    }

}
