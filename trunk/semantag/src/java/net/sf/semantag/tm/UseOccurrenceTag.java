// $Id: UseOccurrenceTag.java,v 1.4 2004/09/14 15:11:01 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Jelly tag allowing to expose an association instance 
 * to the context of its successors.
 * 
 * The association to use may either be specified by
 * the name of a variable that is lookuped in the 
 * jelly context an must be bound to an object of
 * type Association.
 * Otherwise the association may be specified by an
 * id or an adress of a sourceLocator.
 * In this case the association will be searched in 
 * the topicmap that is the current topicmap of this 
 * association.
 * 
 * The current topicmap is either specified by the
 * tmVar-property of this instance or by
 * 
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

    
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Using Ocurrence (ID "+getId()+" / SL: "+getSourceLocator()+")");
        
        // retrieve topic
        if (occurrence == null)
            getOccurrence();

        if (occurrence== null) {
            // failed to retrieve association
            if (shallFailOnNonexistant())
                throw new JellyTagException("Failed to identify occurrence");

            else if (shallAddOnNonexistant())
                occurrence = tmEngine.createOccurrence(getTopicFromContext(null), getId(),
                        getSourceLocator());

            else
                // ignore body
                return;
        }

        // set variable
        storeObject(occurrence);
        
        // process body
        getBody().run(context, output);

    }

    

}