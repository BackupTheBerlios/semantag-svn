// $Id: AddTopicTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

import java.beans.PropertyVetoException;

/**
 * Tag class for creating a new topic instance.
 *
 * @author Niko Schmuck
 * @version 0.1, cf
 *  - can be used to savely retrieve a topic
 *    logging of the fact that a topic with the
 *    given id exists is now optional
 *
 *  - added var-property.
 *    if specified the topic with the given id
 *    is stored under that name in the jelly context
 */
public class AddTopicTag extends BaseTagWithId {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddTopicTag.class);
  private Topic topic;

  // Variable to save the retrieved or created topic in
  private String var;

  // if a true, the tag prints a warning
  // if a topic with the given id already exists
  private boolean warnIfExists = false;

  /**
   * @return Returns the var.
   */
  public String getVar() {
    return var;
  }

  /**
   * @param var
   *            The var to set.
   */
  public void setVar(String var) {
    this.var = var;
  }

  /**
   * @return Returns the warnIfExists.
   */
  public boolean isWarnIfExists() {
    return warnIfExists;
  }

  /**
   * @param warnIfExists
   *            The warnIfExists to set.
   */
  public void setWarnIfExists(boolean warnIfExists) {
    this.warnIfExists = warnIfExists;
  }

  public Topic getTopic() {
    return topic;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    TopicMap tm = getTopicMap();

    // verify that topic does not already exist
    String topicId = getId();

    try {
      topic = tm.getTopicByID(topicId);

      if (topic == null) {
        topic = tm.createTopic(getId());

        if (var != null) {
          context.setVariable(var, topic);
        }

        getBody().run(context, output);
      } else {
        if (warnIfExists) {
          log.info("Topic with id '" + topicId + "' already exists.");
        }

        if (var != null) {
          context.setVariable(var, topic);
        }

        // do not process child elements
      }
    } catch (DuplicateObjectIDException e) {
      throw new JellyTagException(e);
    } catch (PropertyVetoException e) {
      throw new JellyTagException(e);
    }
  }
}
