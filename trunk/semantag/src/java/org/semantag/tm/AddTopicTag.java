// $Id: AddTopicTag.java,v 1.1 2004/10/26 19:49:49 niko_schmuck Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * Tag class for creating a new topic instance.
 * 
 * @author Niko Schmuck
 * @version 0.1, cf - can be used to savely retrieve a topic logging of the fact
 *          that a topic with the given id exists is now optional - added
 *          var-property. if specified the topic with the given id is stored
 *          under that name in the jelly context
 */
public class AddTopicTag extends BaseTMTag implements ContextTopic,
        ReferenceTopicMap {

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddTopicTag.class);

    // The topic that was added by a call to doTag(...)
    private Topic topic = null;

    // The topicmap to which the topic will be added
    private TopicMap tm = null;

    /**
     * @return the topic, that was created by this tag.
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * Creates the topic
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Adding topic (ID "+getId()+" / SL: "+getSourceLocator()+")");
        
        // get map from context
        if (tm == null) {
            tm = getTopicMapFromContext(null);
            if (tm == null) {
                // if tm is still null, this tag must fail
                String msg = "Unable to determine the topicmap to which ";
                msg += "a new topic could be added.";
                throw new JellyTagException(msg);
            }
        }
        
        // create topic
        topic = tmEngine.createTopic(tm, getId(), getSourceLocator());
        
        // set variable
        storeObject(topic);
        
        // process body
        getBody().run(context, output);

    }

    
    /**
     * @return the name of the variable that is used to lookup the topicmap, to
     *         which the new tag will be added
     */
    public TopicMap getTopicmap() {
        return tm;
    }

    /**
     * sets the name of the variable that holds the topicmap, to which the new
     * tag will be added
     */
    public void setTopicmap(TopicMap tm) {
        this.tm = tm;
    }
}