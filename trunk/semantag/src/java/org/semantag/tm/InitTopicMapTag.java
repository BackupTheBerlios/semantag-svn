// $Id: InitTopicMapTag.java,v 1.4 2004/12/29 21:30:26 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;

import org.tm4j.net.Locator;

import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProviderException;

/**
 * Creates a new topic map and stores it in the context.
 * 
 * If there is currently no default topicmap then this topicmap
 * will become the default one.
 * 
 *   
 * The default topicmap is used every time a tag needs to access 
 * a topicmap and is neither nested inside
 * the body of an element that provides one nor does the tag itself
 * explicitly states a topicmap.
 * 
 * @jelly
 *  name="init"
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class InitTopicMapTag extends BaseTopicMapTag {

    protected void validate() throws MissingAttributeException {
        if (getBaselocator() == null) {
            throw new MissingAttributeException(
                    "Attribute baselocator must be specified");
        }
    }

    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {
        log.debug("Creating new topicmap");

        validate();

        initialise(org.tm4j.topicmap.memory.TopicMapProviderFactoryImpl.class);

        TopicMap tm = createTopicMap(createLocator(getBaselocator()));

        storeTopicMap(tm);

        // process body
        getBody().run(context, output);

    }

    protected TopicMap createTopicMap(Locator baseLoc) throws JellyTagException {
        try {
            return tm_provider.createTopicMap(baseLoc);
        } catch (TopicMapProviderException e) {
            throw new JellyTagException("Could not create a new topic map: "
                    + e.toString());
        }
    }
    

}