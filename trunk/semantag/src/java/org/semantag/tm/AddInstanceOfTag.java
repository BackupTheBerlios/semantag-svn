// $Id: AddInstanceOfTag.java,v 1.1 2004/10/26 19:49:49 niko_schmuck Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Tag for typing a topic or an association.
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class AddInstanceOfTag extends BaseTopicReferenceTag {
    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddInstanceOfTag.class);


    /**
     * The TopicMapObject that shall be marked as being an 
     * instance of the given type.
     */
    private TopicMapObject instance;

    /**
     * validates that both <code>parent</code> and <code>type</code> are
     * specified.
     */
    private void validate() throws MissingAttributeException, JellyTagException {
        if (getTopic() == null) {
            String msg = "AddInstanceOfTag requires that a Topic is referenced as the 'type'. ";
            msg +=" This is done either by setting the attribute 'type' to a topic or by using ";
            msg +="one of the various attributes of the TopicReference.";
            throw new MissingAttributeException(msg);
        }

        if (getInstance() == null) {
            String msg = "AddInstanceOfTag requires attribute 'instance' set to either ";
            msg += "a Topic, an Association or an Occurrence.";
            throw new MissingAttributeException(msg);

        }

    }

    /**
     * The instance is either specified explicitly by the <code>instance</code>
     * -property of this class or implicitly by a topic, association or
     * occurrence that forms the context of this tag.
     * 
     * @return the instance for which a type will be set.
     *  
     */
    public TopicMapObject getInstance() throws JellyTagException {

        if (instance != null)
            return instance;

        instance = getTypeableFromContext();
        
        return instance;
        


    }

    /**
     * Adds a type to an instance 
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Add InstanceOf (Type "+getType()+" / Instance: "+getInstance()+")");

        validate();

        tmEngine.addType(getInstance(), getTopic());
    }

    /**
     * @return the type that the instance shall be an instance of
     */
    public Topic getType() throws JellyTagException{
        // calls the BaseClass. This method only exists for
        // the purpose of a more expressive attributename
        return getTopic();
    }

    /**
     * set the type that the instance shall be an instance of
     */
    public void setType(Topic type) {
        // calls the BaseClass. This method only exists for
        // the purpose of a more expressive attributename
        super.setTopic(type);
    }
    
    
    /**
     * set the TopicMapObject which shall become an instance
     * of the type specified by the <code>type</code>-property
     * @param instance
     */
    public void setInstance(TopicMapObject instance) {
        this.instance = instance;
    }
}