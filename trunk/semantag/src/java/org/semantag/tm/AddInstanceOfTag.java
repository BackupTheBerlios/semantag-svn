// $Id: AddInstanceOfTag.java,v 1.5 2004/12/29 21:30:26 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Sets the type of an instance. 
 * <br/><br/>
 * The instance may be a topic, an association or an occurrence.<br/>
 * If the instance is a topic, the type will be added to the set of types of this topic. 
 * If the instance is either an association or an occurrence the type will be set as
 * the single type of this instance.<br/><br/>
 * 
 * 
 * The instance that will be typed is either set
 * explicitly with the <code>instance</code> attribute.<br/>
 * If no instance is explicitly specified, the closest typeable object
 * will be fetched from the context. This is done by descending the xml-tree down to
 * the root until a tag is encountered that specify either a topic, an association
 * or an occurrence.<br/><br/>
 * 
 * The <code>id-</code> and/or the <code>sourceLocator-</code> attributes are ignored.<br/><br/>
 * 
 * To specify the topic that will act as the type, you use either the <code>type</code>
 * attribute or one of the various topic resolving attributes.
 * 
 * @jelly
 *  name="addInstanceOf"
 *  attributes="Additionally this tag supports the 
 *   <a href='/referencingTopics.html'>common set of 
 *   topic-referencing attributes</a> to specify the topic that will be used as the type."
 * 
 * @author Niko Schmuck
 * @author cf
 */

public class AddInstanceOfTag extends BaseTopicReferenceTag {
    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddInstanceOfTag.class);


    /**
     * The TopicMapObject that shall be set to be an 
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
     * The topic that shall become the type of the instance
     */
    public void setType(Topic type) {
        // calls the BaseClass. This method only exists for
        // the purpose of a more expressive attributename
        super.setTopic(type);
    }
    
    
    /**
     * The Topic, Association or Occurrence which will become an instance
     * of the type specified by the <code>type</code>-property
     * @param instance
     */
    public void setInstance(TopicMapObject instance) {
        this.instance = instance;
    }
}