package net.sf.semantag.tm;

import org.tm4j.topicmap.TopicMap;

/**
 * Implemented by classes that provide a topicMap to 
 * the context of their successors.
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public interface ContextTopicMap {

    public TopicMap getTopicMap();
}
