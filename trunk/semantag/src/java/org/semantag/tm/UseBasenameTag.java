// $Id: UseBasenameTag.java,v 1.1 2004/10/26 19:49:49 niko_schmuck Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.BaseName;
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

            doTag(basename, output);
            
//        if (basename == null) {
//            // failed to retrieve association
//            if (shallFailOnNonexistant())
//                throw new JellyTagException("Failed to identify occurrence");
//
//            else if (shallAddOnNonexistant()){
//
//
//                basename = tmEngine.createBasename(
//                        getTopicFromContext(), name, getId(), getSourceLocator());
//            }
//            else {
//                // set var, ignore body
//                storeObject(null);
//                return;
//            }
//        }
//
//        // set variable
//        storeObject(basename);
//
//        // process body
//        getBody().run(context, output);

    }

    
    /**
     * Called from the superclass, when this tag must add a new object to the topicmap
     */
    protected TopicMapObject createTMO() throws JellyTagException {
        basename = tmEngine.createBasename(
                getTopicFromContext(), name, getId(), getSourceLocator());
        return basename;
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
     * sets the basename that this tag shall use
     * @param bn
     */
    public void setBaseName(BaseName bn){
        basename = bn;
    }
}