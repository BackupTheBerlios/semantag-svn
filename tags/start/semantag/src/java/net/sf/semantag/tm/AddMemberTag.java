// $Id: AddMemberTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Topic;

import java.beans.PropertyVetoException;

/**
 * This tag creates a new member for a specified association.
 * Does not support child elements currently.
 *
 * @author Niko Schmuck
 */
public class AddMemberTag extends BaseTagWithId {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddMemberTag.class);
  private String playerId;
  private String roleTopicId;
  private String associationId;

  public void setPlayer(String playerId) {
    this.playerId = playerId;
  }

  public void setRole(String roleTopicId) {
    this.roleTopicId = roleTopicId;
  }

  public void setAssociation(String associationId) {
    this.associationId = associationId;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    // TODO: add validate method
    Association assoc = getCorrespondingAssociation(associationId, log);

    if (assoc != null) {
      try {
        // TODO: retrieve existing member, if it already exists
        Member member = assoc.createMember(getId());

        // add a player to this association member
        Topic player = getTopicById(playerId);

        if (player != null) {
          member.addPlayer(player);
        } else {
          log.warn("No topic object found for id: " + playerId +
                   " to add as player to association member.");
        }

        // set type of this member
        Topic roleTopic = getTopicById(roleTopicId);

        if (player != null) {
          member.setRoleSpec(roleTopic);
        } else {
          log.warn("No topic object found to specifying the member type.");
        }
      } catch (DuplicateObjectIDException e) {
        throw new JellyTagException(e);
      } catch (PropertyVetoException e) {
        throw new JellyTagException(e);
      }
    } else {
      throw new JellyTagException("Member-Tag must be nested in Association-Tag");
    }
  }
}
