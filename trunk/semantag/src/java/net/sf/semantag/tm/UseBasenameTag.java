// $Id: UseBasenameTag.java,v 1.7 2004/09/16 14:02:59 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Jelly tag allowing to expose an basename instance to the context of its
 * successors.
 * 
 * The basename to use may either be specified by the name of a variable that is
 * lookuped in the jelly context an must be bound to an object of type BaseName.
 * Otherwise the basename may be specified by an id or an adress of a
 * sourceLocator. In this case the basename will be searched in the topicmap
 * that is the current topicmap of this basename.
 * 
 * The current topicmap is either specified by the tmVar-property of this
 * instance or by
 * 
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class UseBasenameTag extends BaseUseTag implements ContextBaseName {

    // The Log to which logging calls will be made.
    private static final Log log = LogFactory.getLog(UseBasenameTag.class);

    // name data that is used, if this tag leads to the creation
    // of a basename. ignored otherwise
    private String name;

    // the name of a variable that is bound to a topic that
    // shall be used as a scoping theme.
    // Ignored, if this tag does not lead to the creation of a basename
    private String scopingTopicVar;

    // basename to which this tag refers to
    private BaseName basename;

    /**
     * Resolves the basename that this Tag refers to.
     * 
     * @return @throws
     *         JellyTagException
     */
    public BaseName getBaseName() throws JellyTagException {

        if (basename != null)
            return basename;

        TopicMapObject o = super.resolve();
        if (o != null && !(o instanceof BaseName)) {
            throw new JellyTagException("Failed to identify BaseName. Found "
                    + o);
        }
        basename = (BaseName) o;
        return basename;

    }

    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if (log.isDebugEnabled())
            log.debug("Using BaseName (ID " + getId() + " / SL: "
                    + getSourceLocator() + ")");

        // retrieve topic
        if (basename == null)
            getBaseName();

        if (basename == null) {
            // failed to retrieve association
            if (shallFailOnNonexistant())
                throw new JellyTagException("Failed to identify occurrence");

            else if (shallAddOnNonexistant()){

                // set scope if specified
                Topic theme = getTopicFromVariable(scopingTopicVar);

                basename = tmEngine.createBasename(
                        getTopicFromContext(null), name, getId(), getSourceLocator());
            }
            else
                // ignore body
                return;
        }

        // set variable
        storeObject(basename);

        // process body
        getBody().run(context, output);

    }

    
    /**
     * @return the name , that will be uses as the name data
     * if this tag leads to the creation of a basename
     */
    public String getName() {
        return name;
    }
    /**
     * sets the name , that will be uses as the name data
     * if this tag leads to the creation of a basename
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * sets the name of a variable that is bound to 
     * the topic that shall be used as the scoping
     * topic for this basename
     * @param scopingTopicId
     */
    public void setScope(String scopingTopicVar) {
      this.scopingTopicVar = scopingTopicVar;
    }

    /**
     * sets the basename that this tag shall use
     * @param bn
     */
    public void setBaseName(BaseName bn){
        basename = bn;
    }
}