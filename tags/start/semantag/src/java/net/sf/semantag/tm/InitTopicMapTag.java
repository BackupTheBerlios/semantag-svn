// $Id: InitTopicMapTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;

import org.tm4j.net.Locator;

import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProviderException;

/**
 * Jelly tag which creates a new topic map and stores it in the context.
 *
 * @author Niko Schmuck
 */
public class InitTopicMapTag extends TopicMapBaseTag {
  protected void validate() throws MissingAttributeException {
    if (getBaselocator() == null) {
      throw new MissingAttributeException("Attribute baselocator must be specified");
    }
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    validate();
    log.debug("Creating new topicmap");
    initialise(org.tm4j.topicmap.memory.TopicMapProviderFactoryImpl.class);

    TopicMap tm = createTopicMap(createBaseLocator());

    putTopicMap(tm);

    // Since we expect empty body, no further execution necessary
  }

  protected TopicMap createTopicMap(Locator baseLoc) throws JellyTagException {
    try {
      return tm_provider.createTopicMap(baseLoc);
    } catch (TopicMapProviderException e) {
      throw new JellyTagException("Could not create a new topic map: " +
                                  e.toString());
    }
  }
}
