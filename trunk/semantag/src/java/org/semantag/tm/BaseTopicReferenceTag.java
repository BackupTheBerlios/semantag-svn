// $Id: BaseTopicReferenceTag.java,v 1.3 2004/12/20 20:49:58 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * Base class for tags that reference a topic
 * 
 * @author cf
 */
public abstract class BaseTopicReferenceTag extends BaseTMTag implements ReferenceTopic{
    
    // the topicResolver-delegate
    private TopicResolver topicResolver = new TopicResolver();

    // private reference to the topic that this tag refers to
    private Topic topic;


    /**
     * Returns the topic that this Tag refers to.
     * 
     * @return
     * @throws JellyTagException
     */
    public Topic getTopic() throws JellyTagException {

        if (topic != null)
            return topic;

        TopicMap tm = getTopicMapFromContext(null);
        try {
            topic = topicResolver.getTopic(tm, context);
        } catch (LocatorFactoryException e) {
            throw new JellyTagException("While resolving topic in TopicMap "
                    + tm, e);
        }
        return topic;

    }

    

    public String getTopicID() {
        return topicResolver.getTopicID();
    }

    public String getTopicName() {
        return topicResolver.getTopicName();
    }

    public String getTopicSourceLocator() {
        return topicResolver.getTopicSourceLocator();
    }

    public String getTopicSubject() {
        return topicResolver.getTopicSubject();
    }

    public String getTopicSubjectIndicator() {
        return topicResolver.getTopicSubjectIndicator();
    }

    /**
     * Sets the topic
     * @jelly
     *    required="no"
     */
    public void setTopic(Topic aTopic) {
        topic = aTopic;
    }

    /**
     * same as attribute <code>id</code>
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setTopicID(String topicID) {
        topicResolver.setTopicID(topicID);
    }

    /**
     * Sets the name 
     * that identifies the topic
     * (not implemented in Semantag 0.1)
     * 
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setTopicName(String topicName) {
        topicResolver.setTopicName(topicName);
    }

    /**
     * same as attribute <code>sourceLocator</code>
     * 
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setTopicSourceLocator(String topicSL) {
        topicResolver.setTopicSourceLocator(topicSL);
    }

    /**
     * Sets the address of the subject 
     * that identifies the topic
     * 
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setTopicSubject(String topicSubject) {
        topicResolver.setTopicSubject(topicSubject);
    }

    /**
     * Sets the address of a subjectIndicator 
     * that identifies the topic
     * 
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setTopicSubjectIndicator(String topicSI) {
        topicResolver.setTopicSubjectIndicator(topicSI);
    }

   
    
    /**
      * Sets the id 
     * that identifies the topic
    */
    public String getId() {
        return topicResolver.getTopicID();
    }

    /**
     * Sets the id 
     * that identifies the topic
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setId(String id) {
        topicResolver.setTopicID(id);
    }

    /**
     * redirected to TopicResolver.getSourceLocator()
     */
    public String getSourceLocator() {
        return topicResolver.getTopicSourceLocator();
    }

    /**
     * Sets the address of a sourceLocator 
     * that identifies the topic
     * 
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setSourceLocator(String sourceLocator) {
        topicResolver.setTopicSourceLocator(sourceLocator);
    }

}