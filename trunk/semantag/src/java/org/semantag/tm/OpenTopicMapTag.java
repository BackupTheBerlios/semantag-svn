// $Id: OpenTopicMapTag.java,v 1.2 2004/12/09 16:37:31 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;

import org.tm4j.net.Locator;

import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProviderException;
import org.tm4j.topicmap.source.SerializedTopicMapSource;
import org.tm4j.topicmap.source.TopicMapSource;

import java.io.FileNotFoundException;

import java.net.MalformedURLException;

/**
 * Jelly tag which opens an existing topic map and stores it in the context.
 * 
 * @jelly
 *  name="openTopicMap"
 * 
 * @author cf
 */
public class OpenTopicMapTag extends BaseTopicMapTag {
    
    protected void validate() throws MissingAttributeException {
        if (getFile() == null) {
            throw new MissingAttributeException(
                    "Attribute file must be specified");
        }
    }

    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {
        log.debug("Open existing topicmap");
        validate();
        initialise(org.tm4j.topicmap.memory.TopicMapProviderFactoryImpl.class);

        TopicMap tm = openTopicMap();

        storeTopicMap(tm);

        // process body
        getBody().run(context, output);


    }

    protected TopicMap openTopicMap() throws JellyTagException {
        try {
            Locator baseLoc = null;

            if (getBaselocator() != null) {
                baseLoc = createLocator(getBaselocator());
            }

            TopicMapSource tmsrc = new SerializedTopicMapSource(getFile(),
                    baseLoc);

            return tm_provider.addTopicMap(tmsrc);
        } catch (TopicMapProviderException e) {
            throw new JellyTagException("Could not create a new topic map: "
                    + e.toString());
        } catch (FileNotFoundException e) {
            throw new JellyTagException("Could not create a new topic map: "
                    + e.toString());
        } catch (MalformedURLException e) {
            throw new JellyTagException("Could not create a new topic map: "
                    + e.toString());
        }
    }
}