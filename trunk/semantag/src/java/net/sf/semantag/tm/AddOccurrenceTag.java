// $Id: AddOccurrenceTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.net.LocatorFactoryException;

import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;

import java.beans.PropertyVetoException;

/**
 * Jelly tag creating a new occurrence for the given topic.
 *
 * @author Niko Schmuck
 */
public class AddOccurrenceTag extends BaseTagWithId {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddOccurrenceTag.class);
  private String data;
  private String locator;
  private String topicId;
  private String scopingTopicId;
  private String typeId;

  public void setData(String data) {
    this.data = data;
  }

  public void setLocator(String locator) {
    this.locator = locator;
  }

  public void setTopic(String topicId) {
    this.topicId = topicId;
  }

  public void setScope(String scopingTopicId) {
    this.scopingTopicId = scopingTopicId;
  }

  public void setType(String typeId) {
    this.typeId = typeId;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    // TODO: validate that only data or locator has been specified
    Topic t = getCorrespondingTopic(topicId, log);

    if (t != null) {
      try {
        if (((data != null) && (data.length() > 0)) ||
            ((locator != null) && (locator.length() > 0))) {
          Occurrence occ = t.createOccurrence(getId());

          if ((data != null) && (data.length() > 0)) {
            occ.setData(data);
          } else if ((locator != null) && (locator.length() > 0)) {
            LocatorFactory locf = getTopicMap().getLocatorFactory();
            Locator l = locf.createLocator("URI", locator);

            occ.setDataLocator(l);
          }

          if (typeId != null) {
            occ.setType(getTopicById(typeId));
          }

          Topic scopingTopic = getScopingTopic(scopingTopicId, log);

          if (scopingTopic != null) {
            occ.addTheme(scopingTopic);
          }
        } else {
          log.warn("Neither occurrence (type: " + typeId +
                   ") data nor locator " + "specified for topic: " + t.getID());
        }
      } catch (DuplicateObjectIDException e) {
        throw new JellyTagException(e);
      } catch (PropertyVetoException e) {
        throw new JellyTagException(e);
      } catch (LocatorFactoryException e) {
        throw new JellyTagException(e);
      }
    } else {
      log.warn("No topic object found to add occurrence '" + data + "' to.");
    }
  }
}
