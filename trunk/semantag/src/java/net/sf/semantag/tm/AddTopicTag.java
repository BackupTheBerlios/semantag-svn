// $Id: AddTopicTag.java,v 1.2 2004/09/06 12:27:38 c_froehlich Exp $
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
 *          that a topic with the given id exists is now optional
 *  - added var-property. if specified the topic with the given id is
 * stored under that name in the jelly context
 */
public class AddTopicTag extends BaseTMTag implements ContextTopic,
        TopicMapReference {

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddTopicTag.class);

    private Topic topic = null;

    private String tmVar = null;

    public Topic getTopic() {
        return topic;
    }

    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {
        TopicMap tm = getTopicMap(tmVar);
        topic = AddTopicTag.createTopic(tm, getId(), getSourceLocator());
        storeObject(topic);
    }

    /**
     * Creates a Topic in the given TopicMap. Optionally the topic is created
     * with the given id and the given sourceLocator
     * 
     * @param tm
     * @param id
     * @param sourceLocator
     * @return @throws
     *         JellyTagException as a wrapper around the various exception that
     *         may by thrown while creating a topic
     */
    protected static Topic createTopic(TopicMap tm, String id,
            String sourceLocator) throws JellyTagException {
        try {
            Topic t = tm.createTopic(id);
            if (sourceLocator != null) {
                Locator loc = tm.getLocatorFactory().createLocator("URI",
                        sourceLocator);
                t.addSourceLocator(loc);
            }
            return t;
        } catch (Exception e) {
            throw new JellyTagException("While creating Topic (ID: " + id
                    + "/SourceLocator: " + sourceLocator, e);
        }
    }

    public String getTmVar() {
        return tmVar;
    }

    public void setTmVar(String tmVar) {
        this.tmVar = tmVar;
    }
}