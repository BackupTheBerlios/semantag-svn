// $Id: AddBasenameTag.java,v 1.5 2004/09/15 14:14:52 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Topic;

/**
 * Creates a base name for a given topic. If the attribute 'name' is not
 * specified the body of this element will be used as concrete data for the
 * name.
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
     * A topic, that is the initial member of the scope for the basename created
     */
    private Topic scope;

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
        assertTopic();
        
        // validation
        validate();


        // create basename
        basename = tmEngine.createBasename(parent, name, scope, getId(),
                getSourceLocator());

        // set variable
        storeObject(basename);

        // process body
        getBody().run(context, output);
    }

    /**
     * sets the Topic to which the new basename will be added
     */
    private void assertTopic() throws JellyTagException{
        Topic t = parent;
        if (t == null)
            t = getTopicFromContext(null);

        if(parent == null){
            parent = getTopicFromContext(null);
            if (parent == null) {
                String msg = "AddBasename must be either the children of an object ";
                msg += "that exports a topic to the context for its successors ";
                msg += "or a variable containig a topic must be specified via the topic-Attribute.";
                throw new JellyTagException(msg);
            }
        }
    }
    
    protected void validate(){
        /*
         * There is nothing to do here.
         * Player and role are allowed to be 
         * null and the existance of the target association
         * is handled by assertAssociation(), 
         *
         */
        
    }

    /**
     * sets the name that will be the name of the new basename if this tag
     * generates one
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
     * Sets the name of a variable that is bound to the topic to which this
     * basename shall be added to
     * 
     * @param topicVar
     */
    public void setTopic(Topic aTopic) {
        this.parent = aTopic;
    }

    /**
     * sets the name of a variable that is bound to the topic that shall be used
     * as the scoping topic for this basename
     * 
     * @param scopingTopicId
     */
    public void setScope(Topic theme) {
        this.scope = theme;
    }
}