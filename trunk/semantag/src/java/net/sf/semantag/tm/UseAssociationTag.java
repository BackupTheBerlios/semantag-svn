// $Id: UseAssociationTag.java,v 1.1 2004/09/09 11:40:06 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.TopicMap;
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
public class UseAssociationTag extends BaseUseTag implements ReferenceAssociation,
        ReferenceTopicMap {
    
    // The Log to which logging calls will be made. 
    private static final Log log = LogFactory.getLog(UseAssociationTag.class);


    // private reference to the association that this tag refers to
    private Association association;

    // the name of a variable that holds a topicmap
    private String tmVar;

    
    /**
     * Resolves the association that this Tag refers to.
     * 
     * @return
     * @throws JellyTagException
     */
    public Association getAssociation() throws JellyTagException {

        if (association != null)
            return association;

        TopicMap tm = getTopicMapFromContext(tmVar);
        TopicMapObject a = tmoResolver.getTopicMapObject(tm, context);
        if(!(a instanceof Association )){
            throw new JellyTagException("Failed to identify association. Found "+a);
        }
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
                association = CreatorUtil.createAssociation(getTopicMapFromContext(tmVar), getId(),
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
     * returns the name of the variable, that holds
     * the association that this tag exposes
     * to the context of its successors.
     * @return the name of a variable that can be accessed through 
     * the JellyContext
     */
    public String getAssocVar() {
        return tmoResolver.getVariable();
    }
  
    /**
     * sets the name of the variable, that holds
     * the association that this tag shall expose
     * to the context of its successors.
     */
    public void setAssocVar(String varname) {
        tmoResolver.setVariable(varname);
    }
    
    /**
     * @return the name of the variable that hold
     * the topicmap to which this tag refers to
     */
    public String getTmVar() {
        return tmVar;
    }

    /**
     * sets the name of the variable, that holds
     * the topicmap to which this tag refers to.
     */
    public void setTmVar(String tmVar) {
        this.tmVar = tmVar;
    }
    

}