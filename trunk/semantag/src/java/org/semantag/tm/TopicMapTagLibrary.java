// $Id: TopicMapTagLibrary.java,v 1.4 2004/12/19 20:45:29 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semantag.csv.CSVTagLibrary;

/**
 * The topic map tag library contains tags that support the creation as well as the querying of topicmaps.
 * 
 * It supports all element of the topicmap standards, except the variantname element.
 *
 * Currently all theeses tags rely on <a href="http://tm4j.org">tm4j</a> as its underlying topicmap engine.
 *
 * @jelly
 *   defNS="xmlns:tm=\"jelly:org.semantag.tm.TopicMapTagLibrary\""
 *
 * @author Niko Schmuck
 * @author cf
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
