// $Id: BaseTag.java,v 1.1 2004/10/26 19:49:49 niko_schmuck Exp $
package org.semantag.tm;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.logging.Log;

import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.utils.IDGenerator;
import org.tm4j.topicmap.utils.IDGeneratorFactory;

import java.util.Iterator;

/**
 * Base class for all topic map jelly tags.
 *
 * @author Niko Schmuck
 */
public abstract class BaseTag extends TagSupport implements Dictionary {
  // cf: made this public to allow testing
  public TopicMap getTopicMap() {
    return (TopicMap) context.getVariable(KEY_TOPICMAP);
  }

  /**
   * Stores the given TopicMap in the context.<br>
   * From now on, it is retreivable by a call to
   * <code>TopicMap getTopicMap()</code>.<br>
   *
   * @param tm
   */
  protected void putTopicMap(TopicMap tm) {
    context.setVariable(KEY_TOPICMAP, tm);

    // Put also ID generator to jelly context
    IDGenerator idGenerator = IDGeneratorFactory.newIDGenerator();

    context.setVariable(KEY_IDGENERATOR, idGenerator);
  }

  protected Topic getTopicById(final String topId) {
    TopicMap tm = getTopicMap();
    Topic topic = null;

    if (topId != null) {
      topic = tm.getTopicByID(normalize(topId));
    }

    return topic;
  }

  protected Topic getCorrespondingTopic(final String topicId, final Log log) {
    Topic t = null;

    if ((topicId != null) && (topicId.length() > 0)) {
      String normId = normalize(topicId);

      log.debug("Trying to retrieve topic with id '" + normId + "'.");
      t = getTopicMap().getTopicByID(normId);
    }

    if (t == null) {
      log.debug("Trying to retrieve topic from parent tag.");

      AddTopicTag topicTag = (AddTopicTag) findAncestorWithClass(AddTopicTag.class);

      if (topicTag == null) {
        // log.info("Neither topic specified nor embedded inside
        // addTopic tag. Parent is "+getParent());
        // log message commented out by cf since it is perfectly valid
        // for an instanceOf-Tag
        // to be nested in association or occurrence rather than in
        // topic
      } else {
        t = topicTag.getTopic();
      }
    }

    return t;
  }

  protected Topic getScopingTopic(final String scopingTopicId, final Log log) {
    Topic t = null;

    if ((scopingTopicId != null) && (scopingTopicId.length() > 0)) {
      String normId = normalize(scopingTopicId);

      log.debug("Trying to retrieve scoping topic with id '" + normId + "'.");
      t = getTopicMap().getTopicByID(normId);
    }

    return t;
  }

  protected Association getCorrespondingAssociation(final String associationId,
                                                    final Log log) {
    Association assoc = null;

    if ((associationId != null) && (associationId.length() > 0)) {
      String normId = normalize(associationId);

      log.debug("Trying to retrieve association with id '" + normId + "'.");

      // TODO: improve performance, better use index for lookup
      Iterator itA = getTopicMap().getAssociationsIterator();

      while (itA.hasNext()) {
        Association currentAssoc = (Association) itA.next();

        if (currentAssoc.getID().equals(normId)) {
          assoc = currentAssoc;

          break;
        }
      }
    }

    if (assoc == null) {
      log.debug("Trying to retrieve association from parent tag.");

      AddAssociationTag associationTag = (AddAssociationTag) findAncestorWithClass(AddAssociationTag.class);

      if (associationTag == null) {
        log.info("Neither association specified nor embedded inside addassociation tag.");
      } else {
        assoc = associationTag.getAssociation();
      }
    }

    return assoc;
  }

  /**
   * Turning a given string into a normalized one, with most special
   * characters removed.
   */
  protected static String normalize(final String source) {
    char[] string = source.toCharArray();
    char[] target = new char[string.length];
    int pos = 0;

    for (int i = 0; i < string.length; i++) {
      switch (string[i]) {
      case '.':
        target[pos++] = '-';

        break;

      case '[':
      case ']':
        target[pos++] = '_';

        break;

      case ' ':
      case '\t':
      case '\n':
      case '\r':
      case '{':
      case '}':
      case '+':
      case '*':
      case '\'':
      case '"':
        break;

      default:
        target[pos++] = string[i];
      }
    }

    return new String(target, 0, pos);
  }

  /**
   * Checks whether data has some non-whitespace
   * characters in it
   * @param data
   * @return true if data is not null and if it
   * contains at least one character that
   * is not whitespace, false otherwise
   */
  protected boolean isSpecified(String data) {
    if ((data == null) || (data.length() == 0) || (data.trim().length() == 0)) {
      return false;
    } else {
      return true;
    }
  }
  
  
}
