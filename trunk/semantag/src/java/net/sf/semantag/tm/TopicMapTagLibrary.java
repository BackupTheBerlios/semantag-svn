// $Id: TopicMapTagLibrary.java,v 1.2 2004/09/20 10:13:18 c_froehlich Exp $
package net.sf.semantag.tm;

import net.sf.semantag.csv.CSVTag;
import net.sf.semantag.csv.SetTag;

import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    registerTag("init", InitTopicMapTag.class);
    registerTag("open", OpenTopicMapTag.class);
    registerTag("merge", MergeTopicMapTag.class);
    registerTag("addAssociation", AddAssociationTag.class);
    registerTag("addBasename", AddBasenameTag.class);
    registerTag("addInstanceOf", AddInstanceOfTag.class);
    registerTag("addMember", AddMemberTag.class);
    registerTag("addOccurrence", AddOccurrenceTag.class);
    registerTag("addPlayer", AddPlayerTag.class);
    registerTag("addScope", AddScopeTag.class);
    registerTag("addTopic", AddTopicTag.class);
    registerTag("addSubjectIndicator", AddSubjectIndicatorTag.class);
    registerTag("save", SaveTopicMapTag.class);
    registerTag("tolog", TologTag.class);
    registerTag("useAssociation", UseAssociationTag.class);
    registerTag("useBasename", UseBasenameTag.class);
    registerTag("useMember", UseMemberTag.class);
    registerTag("useOccurrence", UseOccurrenceTag.class);
    registerTag("useTopic", UseTopicTag.class);
    
  }
}
