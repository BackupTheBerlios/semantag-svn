// $Id: AddOccurrenceTag.java,v 1.3 2004/09/15 14:14:52 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;

/**
 * Jelly tag creating a new occurrence for the given topic.
 * 
 * @author Niko Schmuck
 */
public class AddOccurrenceTag extends BaseTMTag implements ContextOccurrence{

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddOccurrenceTag.class);

    /**
     * The data of the occurrence created.
     */
    private String data;

    /**
     * The adress of the resource of the occurrence created.
     */
    private String resource;

    /**
     * The topic to which the occurrence will be added
     */
    private Topic parent;

    /**
     * The Occurrence created by executing the doTag-Method
     */
    private Occurrence occurrence;

    /**
     * @return the occurrence that was created by this tag
     */
    public Occurrence getOccurrence() throws JellyTagException {
        return occurrence;
    }

    /**
     * validates that either <code>data</code> or <code>resource</code> is
     * specified .
     */
    private void validate(Topic t) throws JellyTagException, MissingAttributeException {

        if (t == null) {
            String msg = "AddOccurrence must be either the children of an object ";
            msg += "that exports a topic to the context for its successors ";
            msg += "or a variable containig a topic must be specified via the topic-Attribute.";
            throw new JellyTagException(msg);
        }

        if (data == null && resource == null) {
            String msg = "The creation of an Occurrence requires either the attribute ";
            msg += "'data' set to some data or the attribute 'resource' set to the ";
            msg += "address of a resource.";
            log.error(msg);
            throw new MissingAttributeException("'data' or 'resource'");
        }
        
        if(data != null && resource != null){
            if(log.isDebugEnabled()){
                String msg = "Both attributes 'data' and 'resource' are specified. ";
                msg +="'Resource' will be ignored.";
                log.debug(msg);
            }
        }
    }
    
    /**
     * Creates an Occurrence and adds it to the parent topic
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        // get Parent
        Topic t = parent;
        if (t == null)
            t = getTopicFromContext(null);

        // validation
        validate(t);
        
        // create occurrence
        occurrence = tmEngine.createOccurrence(t, data, resource, getId(),
                getSourceLocator());

        // set variable
        storeObject(occurrence);

        // process body
        getBody().run(context, output);
    }

    /**
     * sets the data for the new occurrence
     * 
     * @param data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the data for the new occurrence
     */
    public String getData() {
        return data;
    }

    /**
     * sets the address of the resource for the new occurrence
     * 
     * @param data
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return the address of the resource for the new occurrence
     */
    public void setResource(String resource) {
        this.resource = resource;
    }

    /**
     * Sets the topic to which the occurrence shall be added to
     * 
     * @param aTopic
     */
    public void setTopic(Topic aTopic) {
        this.parent = aTopic;
    }

}

