// $Id: UseAssociationTag.java,v 1.6 2004/09/15 10:56:24 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Association;
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
public class UseAssociationTag extends BaseUseTag implements ContextAssociation,
        ReferenceTopicMap{
    
    // The Log to which logging calls will be made. 
    private static final Log log = LogFactory.getLog(UseAssociationTag.class);


    // private reference to the association that this tag refers to
    private Association association;


    
    /**
     * Resolves the association that this Tag refers to.
     * 
     * @return
     * @throws JellyTagException
     */
    public Association getAssociation() throws JellyTagException {

        if (association != null)
            return association;

        TopicMapObject a = super.resolve();
        if (a != null && !(a instanceof Association)) {
            throw new JellyTagException("Failed to identify association. Found "+a);
        }
        association = (Association)a;
        return association;

    }

    
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Using Association (ID "+getId()+" / SL: "+getSourceLocator()+")");
        
        // retrieve topic
        if (association == null)
            getAssociation();

        if (association == null) {
            // failed to retrieve association
            if (shallFailOnNonexistant())
                throw new JellyTagException("Failed to identify association");

            else if (shallAddOnNonexistant())
                association = tmEngine.createAssociation(getTopicMapFromContext(), getId(),
                        getSourceLocator());

            else
                // ignore body
                return;
        }

        // set variable
        storeObject(association);
        
        // process body
        getBody().run(context, output);

    }

    
    
 
    
    /**
     * Sets the association that this tag should use
     * @param association
     */
    public void setAssociation(Association association) {
        this.association = association;
    }
}