// $Id: UseAssociationTag.java,v 1.3 2004/12/09 21:19:58 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Retrieves an Association instance and sets it as the
 * context-association for nested tags.
 * 
 * The <code>var</code>-attribute allows to store the association in
 * a variable in order to use it elsewhere in the script.
 * 
 * The nonexistant - attribute triggers what will happen
 * if the specified association could not be found. 
 * 
 * 
 * 
 * @jelly
 *  name="useAssociation"
 * 
 * @jelly.nested 
 *  name="addInstanceOf" 
 *  desc="sets the type for this association" 
 *  required="no"
 * 
 * @jelly.nested 
 *  name="addMember" 
 *  desc="adds a member to this association" 
 *  required="no"
 * 
 * @jelly.nested 
 *  name="addScope" 
 *  desc="adds a topic to the set of scoping topics for this association" 
 *  required="no"
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
        
        doTag(association,output);

    }

    
    
    /**
     * Called from the superclass, when this tag must add a new object to the topicmap
     */
    protected TopicMapObject createTMO() throws JellyTagException {
        association = tmEngine.createAssociation(getTopicMapFromContext(), getId(),
              getSourceLocator());    
    
        return association;
    }
    
    
    /**
     * Sets the association 
     * 
     * @jelly
     *    required="no"
     * 
     * @param association
     */
    public void setAssociation(Association association) {
        this.association = association;
    }
}