// $Id: AddBasenameTag.java,v 1.4 2004/12/29 21:30:26 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Topic;

/**
 * Creates a new basename.
 * <br/><br/>
 * The basename is either created for the topic that is specified by the <code>topic</code>
 * attribute or for the current context topic.<br/><br/>
 * 
 * The <code>id-</code> and/or the <code>sourceLocator-</code> attributes allow you to specify an 
 * id / a sourceLocator 
 * for the new basename. If the underlying tm-engine detects a conflict 
 * (i.e. duplicate id/ sourceLocator) the execution of the tag will fail.<br/><br/>
 * 
 * The name of the basename is specified via the <code>name</code> attribute.<br/>
 * @jelly
 *  name="addBasename"
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class AddBasenameTag extends BaseTMTag implements ContextBaseName {

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddBasenameTag.class);

    /**
     * The data of the basename that will be created by this tag.
     */
    private String name;

    /**
     * The topic to which the basename will be added
     */
    private Topic parent;


    /**
     * The instance of BaseName, that was added by executing the doTag-Method
     */
    private BaseName basename;

    /**
     * @return the basename that was added by this tag
     */
    public BaseName getBaseName() throws JellyTagException {
        return basename;
    }

    /**
     * Adds a BaseName
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Add BaseName (Name: "+getName()+")");


        // get Parent
        assertContext();
        
        // validation
        validate();


        // create basename
        basename = tmEngine.createBasename(parent, name, getId(),
                getSourceLocator());

        // set variable
        storeObject(basename);

        // process body
        getBody().run(context, output);
    }

    /**
     * sets the Topic to which the new basename will be added
     */
    private void assertContext() throws JellyTagException{
        
        if(parent == null){
            parent = getTopicFromContext();
            if (parent == null) {
                String msg = "AddBasename must be either the children of an object ";
                msg += "that exports a topic to the context for its successors ";
                msg += "or a variable containig a topic must be specified via the topic-Attribute.";
                throw new JellyTagException(msg);
            }
        }
    }
    
    /**
     * ensures that a name is provided
     */
    protected void validate() throws JellyTagException {
        if (name == null || name.length() == 0) {
            String msg = "You must provide the basename-tag with a name (via the 'name'-attribute)";
            throw new JellyTagException(msg);

        }
    }


    /**
     * The namedata of the new basename
     * 
     * @jelly
     *  required="true"
     * 
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * The topic to which this 
     * basename will be added
     * 
     * @param topicVar
     */
    public void setTopic(Topic aTopic) {
        this.parent = aTopic;
    }

  
}