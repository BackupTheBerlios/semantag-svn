package net.sf.semantag.tm;

import java.io.InputStream;
import java.util.Properties;

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
public class BaseTMTagTest extends TestCase {

    /**
     * 
     */
    public BaseTMTagTest() {
        super();
    }

    /**
     * @param name
     */
    public BaseTMTagTest(String name) {
        super(name);
    }

    protected TopicMap getTopicMapFromResource(String resource,
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

        InputStream is = getClass().getResourceAsStream(resource);

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

}
