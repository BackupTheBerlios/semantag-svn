// $Id: AddTopicTag.java,v 1.5 2004/09/07 15:07:06 c_froehlich Exp $
package net.sf.semantag.tm;

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

    // The name of a variable that is bound to a topicmap
    private String tmVar = null;

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
        TopicMap tm = getTopicMapFromContext(tmVar);
        
        // create topic
        topic = CreatorUtil.createTopic(tm, getId(), getSourceLocator());
        
        // set variable
        storeObject(topic);
        
        // process body
        getBody().run(context, output);

    }

    
    /**
     * @return the name of the variable that is used
     * to lookup the topicmap, to which the new tag will be 
     * added
     */
    public String getTmVar() {
        return tmVar;
    }

    /**
     * sets the name of the variable that holds the topicmap, 
     * to which the new tag will be added
     */
    public void setTmVar(String tmVar) {
        this.tmVar = tmVar;
    }
}