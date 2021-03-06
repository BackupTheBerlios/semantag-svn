package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.tm4j.topicmap.Topic;

/**
 * To be implemented by classes that provide a topic to 
 * the context for their successors.
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public interface ContextTopic {

    public Topic getTopic() throws JellyTagException;
}
