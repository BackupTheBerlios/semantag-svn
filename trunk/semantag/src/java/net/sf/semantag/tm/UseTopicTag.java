// $Id: UseTopicTag.java,v 1.7 2004/09/12 16:57:34 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * Jelly tag allowing to retrieve a topic instance and sets it as the
 * context-topic for subsequent tags
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

    // the name of a variable that holds a topicmap
    //private String tmVar;

    /**
     * Returns the topic that this Tag refers to.
     * 
     * @return
     * @throws JellyTagException
     */
    public Topic getTopic() throws JellyTagException {

        if (topic != null)
            return topic;

        TopicMap tm = getTopicMapFromContext(getTmVar());
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

        if (topic == null) {
            // failed to retrieve topic
            if (shallFailOnNonexistant())
                throw new JellyTagException("Failed to identify topic");

            else if (shallAddOnNonexistant())
                topic = CreatorUtil.createTopic(getTopicMapFromContext(getTmVar()), getId(),
                        getSourceLocator());

            else
                // ignore body
                return;
        }

        // set variable
        storeObject(topic);
        
        // process body
        getBody().run(context, output);

    }

    public String getTopicVar() {
        return topicResolver.getTopicVar();
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

    public void setTopicVar(String topic) {
        topicResolver.setTopicVar(topic);
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

    
    /**
     * redirected to TopicResolver.getTopicVar()
     */
    public String getFromVar() {
        return topicResolver.getTopicVar();
    }

    /**
     * redirected to TopicResolver.setTopicVar(String)
     */
    public void setFromVar(String varname) {
        topicResolver.setTopicVar(varname);
    }
}