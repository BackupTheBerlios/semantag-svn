//$Id: SetSubjectTag.java,v 1.3 2004/12/29 21:30:26 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;

/**
 * Sets the subject of a topic.
 * <br/><br/>
 * 
 * The topic is either specified explicitly by the topic-attribute
 * or the current context topic is used.
 *
 * @jelly
 *  name="setSubject"
 * 
 * @author cf
 */
public class SetSubjectTag extends BaseTMTag {

  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(SetSubjectTag.class);
  
  /**
   * The adress of the subject locator
   */
  private String locator;

  /**
   * The topic of which the subject will be set.
   */
  private Topic topic;
  
  /**
   * The adress of the locator of the subject
   * 
   * @jelly
   *    required="yes"
   * 
   * @param locator the adress of the locator of the subject
   */
  public void setLocator(String locator) {
    this.locator = locator;
  }
  
  /**
   * The topic of which the subject will be set
   * 
   * @jelly
   *    required="no, as long as a context topic is available"
   * 
   * @param topic the topic of which the subject will be set
   */
  public void setTopic(Topic topic) {
    this.topic = topic;
  }

  /**
   * Sets the subject of a topic
   */
  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {

      if(log.isDebugEnabled())
          log.debug("Set Subject (Locator-Adress: "+locator);

      // assert that a topic is accessible via the context
      validateContext();
      
      // set the subject 
      tmEngine.setSubject(topic, locator);
      
  }
  
  /**
   * determines the Topic for which the  subject will be set
   */
  protected void validateContext() throws JellyTagException{
      if(topic == null){
          topic = getTopicFromContext();
          if(topic == null){
                  String msg = "SetSubject must be either the children of an object ";
                  msg += "that exports a topic to the context for its successors ";
                  msg += "or a variable containig a Topic must be specified via the attribute 'topic'.";
                  throw new JellyTagException(msg);

          }
      }
  }
  
  
}
