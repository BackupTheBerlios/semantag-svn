package net.sf.semantag.tm;

import org.tm4j.topicmap.TopicMap;

/**
 * This interface contains common semantics
 * to reference a topicMap.
 * 
 * @author cf
 * @version 0.1, created on 03.09.2004
 */
public interface ReferenceTopicMap {

    /**
     * @return the topicmap
     */
    public TopicMap getTopicmap();

    /**
     * sets the topicmap
     * 
     * @param topic
     */
    public void setTopicmap(TopicMap tm);


}