// $Id: UseTopicTag.java,v 1.4 2004/12/20 20:49:58 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Retrieves a Topic instance and sets it as the
 * context-topic for nested tags.
 * 
 * The <code>var</code>-attribute allows to store the topic in
 * a variable in order to use it elsewhere in the script.
 * 
 * The nonexistant - attribute triggers what will happen
 * if the specified topic could not be found. 
 * 
 * @jelly
 *  name="useTopic"
 *  attributes="Additionally this tag supports the 
 *   <a href='/referencingTopics.html'>common set of topic-referencing attributes</a>"
 * 
 * @jelly.nested 
 *  name="setSubject" 
 *  desc="sets the subject for this topic" 
 *  required="no"
 * 
 * @jelly.nested 
 *  name="addSubjectIndicator" 
 *  desc="adds a subject indicator for this topic" 
 *  required="no"
 * 
 * @jelly.nested
 *  name="addBasename"
 *  desc="adds a basename to this topic"
 *  required="no"
 * 
 * @jelly.nested
 *  name="addOccurrence"
 *  desc="adds an occurrence to this topic"
 *  required="no"
 * 
 * @jelly.nested 
 *  name="addInstanceOf" 
 *  desc="sets a type for this topic" 
 *  required="no"
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class UseTopicTag extends BaseUseTag implements ReferenceTopic,
        ReferenceTopicMap, ContextTopic {
    
    // The Log to which logging calls will be made. 
    private static final Log log = LogFactory.getLog(UseTopicTag.class);

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

        TopicMap tm = getTopicMapFromContext();
        try {
            topic = topicResolver.getTopic(tm, context);
        } catch (LocatorFactoryException e) {
            throw new JellyTagException("While resolving topic in TopicMap "
                    + tm, e);
        }
        return topic;

    }

    
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Using topic (ID "+getId()+" / SL: "+getSourceLocator()+")");
        
        // retrieve topic
        if (topic == null)
            getTopic();

        doTag(topic, output);

    }
    
    /**
     * Called from the superclass, when this tag must add a new object to the topicmap
     */
    protected TopicMapObject createTMO() throws JellyTagException {
        topic = tmEngine.createTopic(getTopicMapFromContext(), getId(),
                getSourceLocator());
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
     * 
     * @jelly
     *    required="no"
     *    ignore="true"
     */
    public void setTopic(Topic aTopic) {
        topic = aTopic;
    }

    /**
     * Sets the id 
     * that identifies the topic
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
     * Sets the address of a sourceLocator 
     * that identifies the topic
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