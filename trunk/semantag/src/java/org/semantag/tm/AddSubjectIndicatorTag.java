//$Id: AddSubjectIndicatorTag.java,v 1.2 2004/11/29 16:11:04 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;

/**
 * Creates a subject indicator.
 * 
 * The subject indicator is either created for the topic that is 
 * explicitly specified by the <code>topic</code> 
 * attribute. If no topic is explicitly specified, the 
 * subject indicator is created for the current context topic.
 * 
 * The <code>id-</code> and/or the <code>sourceLocator-</code> attributes are ignored
 * 
 * To specify the locator that indicates the subject, you use the <code>locator</code>
 * attribute.
 * 
 * @author Niko Schmuck
 * @author cf
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
   * The address of the locator that indicates the subject
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

      
      // get topic from context
      assertContext();
      
      // validate
      validate();
      

      // add the subject indicator
      tmEngine.addSubjectIndicator(topic, locator);
      
  }
  
  /**
   * determines the Topic to which the new subjectIndicator will be added
   */
  protected void assertContext() throws JellyTagException{
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
  
  /**
   * ensures that a locator adress is provided
   */
  protected void validate() throws JellyTagException {
      if (locator == null || locator.length() == 0) {
          String msg = "You must provide the subjectIndicator-tag with a locator adress (via the 'locator'-attribute)";
          throw new JellyTagException(msg);

      }
  }

  
}
