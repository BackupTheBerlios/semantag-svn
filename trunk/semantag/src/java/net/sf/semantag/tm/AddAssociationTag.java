// $Id: AddAssociationTag.java,v 1.5 2004/09/14 15:51:00 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.TopicMap;

/**
 * Creates an association within the context's topic map under the specified ID.
 * If no ID was explicitly given the ID will be auto-generated.
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
     * @return the name of the variable that is used to lookup the topicmap, to
     *         which the new tag will be added
     */
    public TopicMap getTopicmap() {
        return tm;
    }

    /**
     * sets the name of the variable that holds the topicmap, to which the new
     * tag will be added
     */
    public void setTopicmap(TopicMap tm) {
        this.tm = tm;
    }
}

