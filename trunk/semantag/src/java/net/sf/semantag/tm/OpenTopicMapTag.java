// $Id: OpenTopicMapTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

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
 * @author cf
 */
public class OpenTopicMapTag extends TopicMapBaseTag {
  protected void validate() throws MissingAttributeException {
    if (getFile() == null) {
      throw new MissingAttributeException("Attribute file must be specified");
    }
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    log.debug("Open existing topicmap");
    validate();
    initialise(org.tm4j.topicmap.memory.TopicMapProviderFactoryImpl.class);

    TopicMap tm = openTopicMap();

    putTopicMap(tm);

    // Since we expect empty body, no further execution necessary
  }

  protected TopicMap openTopicMap() throws JellyTagException {
    try {
      Locator baseLoc = null;

      if (getBaselocator() != null) {
        baseLoc = createBaseLocator();
      }

      TopicMapSource tmsrc = new SerializedTopicMapSource(getFile(), baseLoc);

      return tm_provider.addTopicMap(tmsrc);
    } catch (TopicMapProviderException e) {
      throw new JellyTagException("Could not create a new topic map: " +
                                  e.toString());
    } catch (FileNotFoundException e) {
      throw new JellyTagException("Could not create a new topic map: " +
                                  e.toString());
    } catch (MalformedURLException e) {
      throw new JellyTagException("Could not create a new topic map: " +
                                  e.toString());
    }
  }
}
