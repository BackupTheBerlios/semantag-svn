//$Id: AddSubjectIndicatorTag.java,v 1.1 2004/10/26 19:49:49 niko_schmuck Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;

/**
 * Creates a subject indicator for a given topic.
 *
 * @author Niko Schmuck
 */
public class AddSubjectIndicatorTag extends BaseTMTag {

  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddSubjectIndicatorTag.class);
  
  /**
   * The adress of the subjectIndicator locator
   */
  private String locator;

  /**
   * The topic to which the subjectIndicator will be added.
   */
  private Topic topic;
  
  /**
   * @param locator the adress of the locator of this subjectIndicator
   */
  public void setLocator(String locator) {
    this.locator = locator;
  }
  
  /**
   * 
   * @param topic the topic to which the subject indicator will be added
   */
  public void setTopic(Topic topic) {
    this.topic = topic;
  }

  /**
   * Adds a subject indicator to a topic
   */
  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {

      if(log.isDebugEnabled())
          log.debug("Add SubjectIndicator (Locator-Adress: "+locator);

      // assert that a topic to add the subject indicator 
      // to is accessible
      validateContext();
      
      // add the subject indicator
      tmEngine.addSubjectIndicator(topic, locator);
      
  }
  
  /**
   * determines the Topic to which the new subjectIndicator will be added
   */
  protected void validateContext() throws JellyTagException{
      if(topic == null){
          topic = getTopicFromContext();
          if(topic == null){
                  String msg = "AddSubjectIndicator must be either the children of an object ";
                  msg += "that exports a topic to the context for its successors ";
                  msg += "or a variable containig a Topic must be specified via the attribute 'topic'.";
                  throw new JellyTagException(msg);

          }
      }
  }
  
  
}
