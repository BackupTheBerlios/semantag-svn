// $Id: UseBasenameTag.java,v 1.4 2004/12/29 21:30:26 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Retrieves a Basename instance and sets it as the
 * context-basename for nested tags.
 * <br/><br/>
 * The <code>var</code>-attribute allows to store the basename in
 * a variable in order to use it elsewhere in the script.
 * <br/><br/>
 * The nonexistant - attribute triggers what will happen
 * if the specified basename could not be found. 
 * 
 * 
 * 
 * @jelly
 *  name="useBasename"
 *  
 * @jelly.nested 
 *  name="addScope" 
 *  desc="adds a topic to the set of scoping topics for this basename" 
 *  required="no"
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
     * if this tag leads to the creation of a basename.
     * 
     * @jelly 
     *  required="no, unless this tag leads to the creation of a basename"
     */
    public void setName(String name) {
        this.name = name;
    }
    
   

    /**
     * sets the BaseName-Object that this tag shall use
     * @param bn
     */
    public void setBaseName(BaseName bn){
        basename = bn;
    }
}