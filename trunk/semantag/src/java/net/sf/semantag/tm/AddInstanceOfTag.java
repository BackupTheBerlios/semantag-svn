// $Id: AddInstanceOfTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Topic;

import java.beans.PropertyVetoException;

/**
 * Tag for typing a topic or an association.
 *
 * @author Niko Schmuck
 */
public class AddInstanceOfTag extends BaseTag {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddInstanceOfTag.class);
  private String typeId;
  private String topicId;
  private String associationId;

  public void setType(String typeId) {
    this.typeId = typeId;
  }

  public void setTopic(String topicId) {
    this.topicId = topicId;
  }

  public void setAssociation(String associationId) {
    this.associationId = associationId;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    try {
      if (typeId == null) {
        throw new MissingAttributeException("InstanceOf-Tag requires type-attriibute");
      }

      // TODO: that only topic or association has been specified
      Topic type = getTopicById(typeId);

      if (type == null) {
        log.warn("No topic found with id " + typeId);

        return;
      }

      Topic topic = getCorrespondingTopic(topicId, log);

      if (topic != null) {
        if (!topic.isOfType(type)) {
          topic.addType(type);
        }
      } else {
        Association assoc = getCorrespondingAssociation(associationId, log);

        if (assoc != null) {
          assoc.setType(type);
        }
      }
    } catch (PropertyVetoException e) {
      throw new JellyTagException(e);
    }
  }
}
