// $Id: RetrieveTopicTag.java,v 1.1 2004/08/24 00:12:28 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;

import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * Jelly tag allowing to retrieve a topic instance from the topic map
 * as stored in the context.
 *
 * @author Niko Schmuck
 */
public class RetrieveTopicTag extends BaseTag {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(RetrieveTopicTag.class);
  private String id;
  private String subjectIndicator;
  private String sourceLocator;
  private String varName;

  public void setId(String id) {
    this.id = id;
  }

  public void setSubjectIndicator(String subjectIndicator) {
    this.subjectIndicator = subjectIndicator;
  }

  public void setSourceLocator(String sourceLocator) {
    this.sourceLocator = sourceLocator;
  }

  public void setVar(String var) {
    this.varName = var;
  }

  protected Topic getTopic() throws JellyTagException {
    TopicMap tm = getTopicMap();
    Topic topic = null;

    if (id != null) {
      topic = tm.getTopicByID(id);
    }

    if ((topic == null) && (subjectIndicator != null)) {
      try {
        Locator loc = tm.getLocatorFactory().createLocator("URI",
                                                           subjectIndicator);

        topic = tm.getTopicBySubjectIndicator(loc);
      } catch (LocatorFactoryException e) {
        throw new JellyTagException("Could not create a valid URI locator from address: " +
                                    subjectIndicator);
      }
    }

    if ((topic == null) && (sourceLocator != null)) {
      try {
        Locator loc = tm.getLocatorFactory().createLocator("URI", sourceLocator);

        topic = (Topic) tm.getObjectBySourceLocator(loc);
      } catch (LocatorFactoryException e) {
        throw new JellyTagException("Could not create a valid URI locator from address: " +
                                    subjectIndicator);
      }
    }

    return topic;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    // TODO: add validate, one of id, sourceLocator or subjectIndicator has to be set
    Topic topic = getTopic();

    if (topic != null) {
      context.setVariable(varName, topic);
    } else {
      log.info("No topic found to assign to '" + varName + "'.");
    }
  }
}
