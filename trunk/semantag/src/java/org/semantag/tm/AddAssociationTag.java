// $Id: AddAssociationTag.java,v 1.3 2004/12/09 16:37:31 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.TopicMap;

/**
 * Creates an association.
 * 
 * The association is either created in the topicmap that is specified by the <code>topicmap</code>
 * attribute or in the current context topicmap.
 * 
 * The <code>id</code> and/or the <code>sourceLocator</code>- attributes allow you to specify an 
 * id / a sourceLocator
 * for the new association. If the underlying tm-engine detects a conflict (i.e. duplicate id /
 * sourceLocator) the execution of the tag will fail.
 * 
 * @jelly
 *  name="addAssociation"
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class AddAssociationTag extends BaseTMTag implements ContextAssociation,
        ReferenceTopicMap {

    // The Log to which logging calls will be made.
    private static final Log log = LogFactory.getLog(AddAssociationTag.class);

    // The association that was added by a call to doTag(...)
    private Association association;

    // The topicmap to which the association will be added
    private TopicMap tm = null;

    /**
     * @return the association, that was created by this tag.
     */
    public Association getAssociation() {
        return association;
    }

    /**
     * Creates the association, stores it in the context and passes control to
     * the body of the tag.
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if (log.isDebugEnabled())
            log.debug("Adding association (ID " + getId() + " / SL: "
                    + getSourceLocator() + ")");

        // get map from context
        if (tm == null) {
            tm = getTopicMapFromContext(null);
            if (tm == null) {
                // if tm is still null, this tag must fail
                String msg = "Unable to determine the topicmap to which ";
                msg += "a new association could be added.";
                throw new JellyTagException(msg);
            }
        }

        // create association
        association = tmEngine.createAssociation(tm, getId(),
                getSourceLocator());

        // set variable
        storeObject(association);

        // process body
        getBody().run(context, output);

    }

    /**
     * @return the topicmap, to which the new association will be added. 
     * This method returns null if no topicmap was explicitly set.
     */
    public TopicMap getTopicmap() {
        return tm;
    }

    /**
     * The topicmap, to which the new association will be added
     */
    public void setTopicmap(TopicMap tm) {
        this.tm = tm;
    }
    
    
}

