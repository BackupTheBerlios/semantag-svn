// $Id: TopicMapTagLibrary.java,v 1.1 2004/08/24 00:12:28 niko_schmuck Exp $
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
    registerTag("addTopic", AddTopicTag.class);
    registerTag("addBasename", AddBasenameTag.class);
    registerTag("addOccurrence", AddOccurrenceTag.class);
    registerTag("addAssociation", AddAssociationTag.class);
    registerTag("addSubjectIndicator", AddSubjectIndicatorTag.class);
    registerTag("addMember", AddMemberTag.class);
    registerTag("addInstanceOf", AddInstanceOfTag.class);
    registerTag("save", SaveTopicMapTag.class);
    registerTag("retrieveTopic", RetrieveTopicTag.class);
    registerTag("csv", CSVTag.class);
    registerTag("set", SetTag.class);
    registerTag("tolog", TologTag.class);
  }
}
