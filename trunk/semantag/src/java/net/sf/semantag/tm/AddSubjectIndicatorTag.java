//$Id: AddSubjectIndicatorTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.net.LocatorFactoryException;

import org.tm4j.topicmap.Topic;

import java.beans.PropertyVetoException;

/**
 * Creates a subject indicator for a given topic.
 *
 * @author Niko Schmuck
 */
public class AddSubjectIndicatorTag extends BaseTagWithId {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddSubjectIndicatorTag.class);
  private String locator;
  private String topicId;

  public void setLocator(String locator) {
    this.locator = locator;
  }

  public void setTopic(String topicId) {
    this.topicId = topicId;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    Topic t = getCorrespondingTopic(topicId, log);

    if (t != null) {
      LocatorFactory lf = getTopicMap().getLocatorFactory();

      try {
        Locator loc = lf.createLocator("URI", locator);

        t.addSubjectIndicator(loc);
      } catch (LocatorFactoryException e) {
        throw new JellyTagException("Could not create locator from '" +
                                    locator + "'", e);
      } catch (PropertyVetoException e) {
        throw new JellyTagException(e);
      }
    } else {
      log.warn("No topic object found to add subject indicator '" + locator +
               "' to.");
    }
  }
}
