// $Id: AddOccurrenceTag.java,v 1.3 2004/12/09 16:37:31 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;

/**
 * Creates an occurrence.
 * 
 * The occurrence is either created for the topic that is 
 * explicitly specified by the <code>topic</code> 
 * attribute. If no topic is explicitly specified, the 
 * occurrence is  created for the current context topic.
 * 
 * The <code>id-</code> and/or the <code>sourceLocator-</code> attributes allow you to specify an 
 * id / a sourceLocator 
 * for the new occurrence. If the underlying tm-engine detects a conflict 
 * (i.e. duplicate id/ * sourceLocator) the execution of the tag will fail.
 * 
 * To specify an external resource to which this occurrence shall point, the 
 * <code>resource</code> attribute is used. For an occurrence with internal data, the 
 * <code>data</code> attribute is used. If both attributes are specified, <code>data</code> 
 * will have precedence.
 * 
 * @jelly
 *  name="addOccurrence"
 * 
 * @author Niko Schmuck
 * @author cf
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
     * determines the topic 
     * to which this occurrence will be added
     */
    private void assertContext() throws JellyTagException{
        if(parent == null){
            parent = getTopicFromContext();
            if(parent == null){
                String msg = "AddOccurrence must be either the children of an object ";
                msg += "that exports a topic to the context for its successors ";
                msg += "or a variable containig a topic must be specified via the topic-Attribute.";
                    throw new JellyTagException(msg);

            }
        }
    }

    /**
     * validates that either <code>data</code> or <code>resource</code> is
     * specified .
     */
    private void validate() throws JellyTagException, MissingAttributeException {


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
        assertContext();

        // validation
        validate();
        
        // create occurrence
        occurrence = tmEngine.createOccurrence(parent, data, resource, getId(),
                getSourceLocator());

        // set variable
        storeObject(occurrence);

        // process body
        getBody().run(context, output);
    }

    /**
     * The inline data for the new occurrence
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
     * The address of the external resource for the new occurrence
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
     * The topic to which the new occurrence will be added
     * 
     * @param aTopic
     */
    public void setTopic(Topic aTopic) {
        this.parent = aTopic;
    }

}

