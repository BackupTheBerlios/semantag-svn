// $Id: AddTopicTag.java,v 1.3 2004/09/06 19:40:55 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.DuplicateResourceLocatorException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

import java.beans.PropertyVetoException;

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
        TopicMapReference {

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
        TopicMap tm = getTopicMap(tmVar);
        topic = AddTopicTag.createTopic(tm, getId(), getSourceLocator());
        storeObject(topic);
    }

    
    /**
     * Creates a Topic in the given TopicMap. 
     * 
     * If specified the topic is created
     * with the given id and the given sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return 
     * @throws JellyTagException as a wrapper around the various exceptions
     *  that may by thrown while creating a topic
     */
    protected static Topic createTopic(TopicMap tm, String id,
            String sourceLocator) throws JellyTagException {

        // Check the sourceLocator first.
        // Otherwise the topic would have been already
        // created, before we realize a potential
        // duplication of sourceLocators
        Locator sl = null; 
        if(sourceLocator != null){
            try {
                sl = tm.getLocatorFactory().createLocator("URI",
                        sourceLocator);
            } catch (LocatorFactoryException e1) {
                String msg = "While creating Topic (ID: ";
                msg += id + "/SourceLocator: ";
                msg += sourceLocator;
                throw new JellyTagException(msg, e1);
            }
            
            if(tm.getObjectBySourceLocator(sl) != null){
                // SourceLocator already points to an object
                // in the topicmaps
                String msg = "Illegal attempt to add a topic with an ";
                msg += "already existing sourceLocator ("+sourceLocator+").";
                throw new JellyTagException(msg);
            }
        }
        
        try {
            Topic t = tm.createTopic(id);
            if (sl != null) {
                t.addSourceLocator(sl);
            }
            return t;
        } catch (Exception e) {
            throw new JellyTagException("While creating Topic (ID: " + id
                    + "/SourceLocator: " + sourceLocator, e);
        }
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