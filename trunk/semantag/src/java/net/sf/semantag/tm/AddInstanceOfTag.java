// $Id: AddInstanceOfTag.java,v 1.2 2004/09/15 10:56:24 c_froehlich Exp $
package net.sf.semantag.tm;

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
public class AddInstanceOfTag extends BaseTMTag {
    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddInstanceOfTag.class);

    private Topic type;

    private TopicMapObject instance;

    /**
     * validates that both <code>parent</code> and <code>type</code> are
     * specified.
     */
    private void validate() throws MissingAttributeException, JellyTagException {
        if (type == null) {
            String msg = "AddInstanceOfTag requires attribute 'type' set to a Topic";
            throw new MissingAttributeException(msg);
        }

        if (getInstance() == null) {
            String msg = "AddInstanceOfTag requires attribute 'parent' set to either ";
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

        if ((instance = getTopicFromContext(null)) != null) {
            return instance;
        }
        if ((instance = getAssociationFromContext(null)) != null) {
            return instance;
        }
        if ((instance = getOccurrenceFromContext()) != null) {
            return instance;
        }

        return null;

    }

    /**
     * Adds a type to the parent specified
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Add InstanceOf (Type "+getType()+" / Instance: "+getInstance()+")");

        validate();

        tmEngine.addType(getInstance(), type);
    }

    /**
     * @return the type that the instance shall be an instance of
     */
    public Topic getType() {
        return type;
    }

    /**
     * set the type that the instance shall be an instance of
     */
    public void setType(Topic type) {
        this.type = type;
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