// $Id: AddScopeTag.java,v 1.3 2004/09/20 12:02:51 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Tag for adding a topic to the set of topics that constitutes
 * the scope of a BaseName, an Association or an Occurrence.
 * 
 * @author Niko Schmuck
 * @author cf
 */
public class AddScopeTag extends BaseTopicReferenceTag {
    
    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(AddScopeTag.class);


    /**
     * The TopicMapObject to whose scope the theme is added to..
     */
    private TopicMapObject scopedObject;

    /**
     * validates that both <code>parent</code> and <code>type</code> are
     * specified.
     */
    private void validate() throws MissingAttributeException, JellyTagException {
        if (getTopic() == null) {
            String msg = "AddScopeTag requires that a Topic is referenced as the 'theme'. ";
            msg +=" This is done either by setting the attribute 'theme' to a topic or by using ";
            msg +="one of the various attributes of the TopicReference.";
            throw new MissingAttributeException(msg);
        }

        if (getScopedObject() == null) {
            String msg = "AddScopeTag requires attribute 'scopedObject' set to either ";
            msg += "a BaseName, an Association or an Occurrence.";
            throw new MissingAttributeException(msg);

        }

    }

    /**
     * The scoped object is either specified explicitly by the <code>scopedObject</code>
     * -property of this class or implicitly by a basename, association or
     * occurrence that forms the context of this tag.
     * 
     * @return the instance for which a type will be set.
     *  
     */
    public TopicMapObject getScopedObject() throws JellyTagException {

        if (scopedObject != null)
            return scopedObject;

        scopedObject = getScopeableFromContext();
        
        return scopedObject;
        

    }

    /**
     * Adds a type to the parent specified
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if(log.isDebugEnabled())
            log.debug("Add Scope (Theme "+getTopic()+" / ScopedObject: "+getScopedObject()+")");

        validate();

        tmEngine.addTheme(getScopedObject(), getTopic());
    }

    /**
     * @return the theme that will be added to the set of
     * themes that constitutes the scope of the scopedObject
     */
    public Topic getTheme() throws JellyTagException{
        // calls the BaseClass. This method only exists for
        // the purpose of a meaningful attributename
        return getTopic();
    }

    /**
     * sets the theme that will be added to the set of
     * themes that constitutes the scope of the scopedObject
     */
    public void setTheme(Topic theme) {
        // calls the BaseClass. This method only exists for
        // the purpose of a meaningful attributename
        super.setTopic(theme);
    }
    
    
    /**
     * set the TopicMapObject to whose scope the
     * theme will be added
     * @param sc
     */
    public void setScopedObject(TopicMapObject sc) {
        this.scopedObject = sc;
    }
}