// $Id: BaseTopicReferenceTag.java,v 1.1 2004/09/17 19:47:39 c_froehlich Exp $
package net.sf.semantag.tm;

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


    public void setTopic(Topic aTopic) {
        topic = aTopic;
    }

    public void setTopicID(String topicID) {
        topicResolver.setTopicID(topicID);
    }

    public void setTopicName(String topicName) {
        topicResolver.setTopicName(topicName);
    }

    public void setTopicSourceLocator(String topicSL) {
        topicResolver.setTopicSourceLocator(topicSL);
    }

    public void setTopicSubject(String topicSubject) {
        topicResolver.setTopicSubject(topicSubject);
    }

    public void setTopicSubjectIndicator(String topicSI) {
        topicResolver.setTopicSubjectIndicator(topicSI);
    }

   
    
    /**
     * redirected to TopicResolver.getID()
     */
    public String getId() {
        return topicResolver.getTopicID();
    }

    /**
     * redirected to TopicResolver.setID(String)
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
     * redirected to TopicResolver.setSourceLocator(String)
     */
    public void setSourceLocator(String sourceLocator) {
        topicResolver.setTopicSourceLocator(sourceLocator);
    }

}