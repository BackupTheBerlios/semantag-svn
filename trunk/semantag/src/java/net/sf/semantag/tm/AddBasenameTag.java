// $Id: AddBasenameTag.java,v 1.1 2004/08/24 00:12:28 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Topic;

/**
 * Creates a base name for a given topic.
 * If the attribute 'name' is not specified the body of this element
 * will be used as concrete data for the name.
 *
 * @author Niko Schmuck
 */
public class AddBasenameTag extends BaseTagWithId {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddBasenameTag.class);
  private String name;
  private String topicId;
  private String scopingTopicId;

  public void setName(String name) {
    this.name = name;
  }

  public String getName() throws Exception {
    return ((name != null) ? name : getBodyText());
  }

  public void setTopic(String topicId) {
    this.topicId = topicId;
  }

  public void setScope(String scopingTopicId) {
    this.scopingTopicId = scopingTopicId;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    try {
      Topic t = getCorrespondingTopic(topicId, log);

      if (t != null) {
        BaseName bn = t.createName(getId());

        bn.setData(getName());

        Topic scopingTopic = getScopingTopic(scopingTopicId, log);

        if (scopingTopic != null) {
          bn.addTheme(scopingTopic);
        }
      } else {
        log.warn("No topic object found to add basename '" + name + "' to.");
      }
    } catch (Exception e) {
      throw new JellyTagException(e);
    }
  }
}
