// $Id: AddTopicTag.java,v 1.3 2004/12/09 16:37:31 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * Creates a topic.
 * 
 * The topic is either created in the topicmap that is 
 * explicitly specified by the <code>topicmap</code> 
 * attribute. If no topicmap is explicitly specified, the 
 * new topic is created in the current context topicmap.
 * 
 * The <code>id-</code> and/or the <code>sourceLocator-</code> attributes allow you to specify an 
 * id / a sourceLocator 
 * for the new topic. If the underlying tm-engine detects a conflict 
 * (i.e. duplicate id/ * sourceLocator) the execution of the tag will fail.
 * 
 * @jelly
 *  name="addTopic"
 * 
 * @author Niko Schmuck
 * @author cf
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
        assertContext();
        
        // validate
        validate();
        
        // create topic
        topic = tmEngine.createTopic(tm, getId(), getSourceLocator());
        
        // set variable
        storeObject(topic);
        
        // process body
        getBody().run(context, output);

    }

    

    /**
     * determines the topicmap 
     * to which this topic will be added
     */
    private void assertContext() throws JellyTagException{
        if (tm == null) {
            tm = getTopicMapFromContext(null);
            if (tm == null) {
                // if tm is still null, this tag must fail
                String msg = "Unable to determine the topicmap to which ";
                msg += "a new topic could be added.";
                throw new JellyTagException(msg);
            }
        }
    }


    /**
     * nothing to validate here.
     */
    private void validate() throws MissingAttributeException, JellyTagException {

    }

    
    /**
     * @return the topicmap, to which the new topic will be added. 
     * This method returns null if no topicmap was explicitly set.
     */
    public TopicMap getTopicmap() {
        return tm;
    }

    /**
     * The topicmap, to which the new association will be added
     */
    public void setTopicmap(TopicMap tm) {
        this.tm = tm;
    }
}