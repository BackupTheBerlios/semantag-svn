// $Id: TopicMapTagLibrary.java,v 1.3 2004/12/09 16:37:31 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semantag.csv.CSVTagLibrary;

/**
 * The topic map tag library basically registers all jelly tags,
 * so that they can be accessed from a jelly script under a specific
 * namespace.
 *
 * @author Niko Schmuck
 */
public class TopicMapTagLibrary extends TagLibrary {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(TopicMapTagLibrary.class);

  public TopicMapTagLibrary() {
    log.debug("Registering tags for topic map tag library.");
    registerTag("addAssociation", AddAssociationTag.class);
    registerTag("addBasename", AddBasenameTag.class);
    registerTag("addInstanceOf", AddInstanceOfTag.class);
    registerTag("addMember", AddMemberTag.class);
    registerTag("addOccurrence", AddOccurrenceTag.class);
    registerTag("addPlayer", AddPlayerTag.class);
    registerTag("addScope", AddScopeTag.class);
    registerTag("addTopic", AddTopicTag.class);
    registerTag("addSubjectIndicator", AddSubjectIndicatorTag.class);
    registerTag("label", LabelTag.class);
    registerTag("init", InitTopicMapTag.class);
    registerTag("merge", MergeTopicMapTag.class);
    registerTag("open", OpenTopicMapTag.class);
    registerTag("setSubject", SetSubjectTag.class);
    registerTag("save", SaveTopicMapTag.class);
    registerTag("tolog", TologTag.class);
    registerTag("useAssociation", UseAssociationTag.class);
    registerTag("useBasename", UseBasenameTag.class);
    registerTag("useMember", UseMemberTag.class);
    registerTag("useOccurrence", UseOccurrenceTag.class);
    registerTag("useTopic", UseTopicTag.class);
    
  }
  
}
