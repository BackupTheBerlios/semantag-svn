// $Id: UseOccurrenceTag.java,v 1.3 2004/12/09 21:19:58 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Retrieves an Occurrence instance and sets it as the
 * context-occurrence for nested tags.
 * 
 * The <code>var</code>-attribute allows to store the occurrence in
 * a variable in order to use it elsewhere in the script.
 * 
 * The nonexistant - attribute triggers what will happen
 * if the specified occurrece could not be found. 
 * 
 * 
 * 
 * @jelly
 *  name="useOccurrence"
 * 
 * @jelly.nested 
 *  name="addInstanceOf" 
 *  desc="sets the type for this occurrence" 
 *  required="no"
 * 
 * @jelly.nested 
 *  name="addScope" 
 *  desc="adds a topic to the set of scoping topics for this occurrence" 
 *  required="no"
 * 
 * @author Niko Schmuck
 * @author cf
 */

public class UseOccurrenceTag extends BaseUseTag implements ContextOccurrence
        {
    
    // The Log to which logging calls will be made. 
    private static final Log log = LogFactory.getLog(UseOccurrenceTag.class);


    // occurrence to which this tag refers to
    private Occurrence occurrence;

    /**
     * If this tag leads to the creation of an occurrence this
     * String is used as the data of the occurrence created.
     * Ignored otherwise
     */
    private String data;

    /**
     * If this tag leads to the creation of an occurrence this
     * String is used as the address of the dataLocator 
     * of the occurrence created.
     * Ignored otherwise
     */
    private String resource;


    
    /**
     * Resolves the occurrence that this Tag refers to.
     * 
     * @return
     * @throws JellyTagException
     */
    public Occurrence getOccurrence() throws JellyTagException {

        if (occurrence != null)
            return occurrence;

        TopicMapObject o = super.resolve();
        if (o != null && !(o instanceof Occurrence)) {
            throw new JellyTagException("Failed to identify occurrence. Found "+o);
        }
        occurrence = (Occurrence)o;
        return occurrence;

    }

    /**
     * validates that either <code>data</code> or <code>resource</code> is
     * specified.
     */
    private void validateForCreation() throws JellyTagException {
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

    
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Using Ocurrence (ID "+getId()+" / SL: "+getSourceLocator()+")");
        
        // retrieve topic
        if (occurrence == null)
            getOccurrence();

        doTag(occurrence, output);
//        if (occurrence== null) {
//            // failed to retrieve occurrence
//            if (shallFailOnNonexistant())
//                throw new JellyTagException("Failed to identify occurrence");
//
//            else if (shallAddOnNonexistant()){
//                validateForCreation();
//                occurrence = tmEngine.createOccurrence(getTopicFromContext(), data, resource, getId(),
//                        getSourceLocator());
//            }
//            else {
//                // set var, ignore body
//                storeObject(null);
//                return;
//            }
//        }
//
//        // set variable
//        storeObject(occurrence);
//        
//        // process body
//        getBody().run(context, output);

    }

    
    /**
     * Called from the superclass, when this tag must add a new object to the topicmap
     */
    protected TopicMapObject createTMO() throws JellyTagException {
        validateForCreation();
        occurrence = tmEngine.createOccurrence(getTopicFromContext(), data, resource, getId(),
                getSourceLocator());
        return occurrence;
    }

    /**
     * Sets the Occurrence that this tag should use
     * @param occurrence
     */
    public void setOccurrence(Occurrence occurrence) {
        this.occurrence = occurrence;
    }

    
    /**
     * sets the data that is used for 
     * a new occurrence
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
}