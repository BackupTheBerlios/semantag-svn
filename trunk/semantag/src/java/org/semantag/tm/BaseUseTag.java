package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.XMLOutput;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;



/**
 * BaseTags for all Tags that are able to resolve
 * an existing TopicMapObject.
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public abstract class BaseUseTag extends BaseTMTag implements ReferenceTopicMap{

    public final static String NE_IGNORE_BODY = "ignore";
    public final static String NE_ADD = "add";
    public final static String NE_FAIL = "fail";
    
    // defaults to ignore
    public final static String NE_DEFAULT= NE_IGNORE_BODY;
    
    private String nonexistant = NE_DEFAULT;
    
    protected TMOResolver tmoResolver;

    // the name of a variable that holds a topicmap
    private TopicMap tm;

    
    /**
     * 
     */
    public BaseUseTag() {
        super();
        tmoResolver = new TMOResolver(tmEngine);
    }
    
    /**
     * resolves the TopicMapObject to which the
     * current UseTag refers.
     * 
     * @return a TopicMapObject or null, if the 
     * current tag does not refer to an object.
     * @throws JellyTagException if an attempt to
     * refer to an object was made in an apparently 
     * erroneous manner.
     */
    public TopicMapObject resolve()
        throws JellyTagException
    {
        if(tm == null)
            tm = getTopicMapFromContext(null);
        TopicMapObject o = tmoResolver.getTopicMapObject(tm, context);
        return o;
    }

    public boolean shallFailOnNonexistant(){
        return nonexistant.equals(NE_FAIL);
    }

    public boolean shallAddOnNonexistant(){
        return nonexistant.equals(NE_ADD);
    }
    
    public boolean shallIgnoreOnNonexistant(){
        return nonexistant.equals(NE_IGNORE_BODY);
    }
    
    
    public String getNonexistant() {
        return nonexistant;
    }
    
    /**
     * Specifies what will happen, if the referenced TopicMapObject
     * does not exist.<br/>
     * Valid values are "ignore", "add" or "fail".<br/>
     * "ignore": If the TopicMapObject that this tag refers to
     * does exist, than the body of the tag will be executed.
     * Otherwise the body of the tag will be ignored silently<br/>
     * <br/>
     * "add": If the TopicMapObject that this tag refers to
     * does not exist, than a new Object will be created and 
     * the body of the tag will be executed.<br/>
     * If the TopicMapObject already exists, the body of the tag will be ignored silently.<br/>
     * <br/>
     * "fail": If the TopicMapObject that this tag refers to
     * does not exist the execution of the current script will be stopped.
     * 
     * @param nonexistant
     * @jelly
     *    required="no"
     *    default="ignore"
     * 
     */
    public void setNonexistant(String nonexistant) {
        if(nonexistant == null) {
            this.nonexistant = NE_DEFAULT;
        }
        else if(nonexistant.equalsIgnoreCase(NE_FAIL)){
            this.nonexistant = NE_FAIL; 
        }
        else if(nonexistant.equalsIgnoreCase(NE_ADD)){
            this.nonexistant = NE_ADD; 
        }
        else if(nonexistant.equalsIgnoreCase(NE_IGNORE_BODY)){
            this.nonexistant = NE_IGNORE_BODY; 
        }
        else{
            this.nonexistant = NE_DEFAULT; 
        }
    }
        
    /**
     * returns the topicmap that is the context topicmap
     * for the current tag
     * @return
     * @throws JellyTagException
     */
    protected TopicMap getTopicMapFromContext()
        throws JellyTagException
    {
        if(tm == null)
            tm = getTopicMapFromContext(null);
        return tm;
    }
    
    public String getId() {
        return tmoResolver.getId();
    }
    public String getSourceLocator() {
        return tmoResolver.getSourceLocator();
    }
    /**
     * @return the name of the variable that hold
     * the topicmap to which this tag refers to
     */
    public TopicMap getTopicmap() {
        return tm;
    }

    /**
     * Sets the id that is used to lookup the TopicMapObject.<br/>
     * If no TopicMapObject can be found and a new one will be created
     * it will use the given id.
     * 
     * @jelly
     *  required ="no"
     */
    public void setId(String id) {
        tmoResolver.setId(id);
    }
    
    
    /**
     * Sets the address of the sourceLocator that is used to 
     * lookup the TopicMapObject.<br/>
     * If no TopicMapObject can be found and a new one will be created
     * it will use the given sourceLocator.
     * 
     * @jelly
     *  required ="no"
     */
    public void setSourceLocator(String sourceLocator) {
        tmoResolver.setSourceLocator(sourceLocator);
    }
    
    /**
     * Sets the topicmap to which this tag refers to.
     */
    public void setTopicmap(TopicMap tm) {
        this.tm = tm;
    }

    
    /**
     * Implements the standard behavior of use tag.
     * Chooses a strategy dependant on whether the topicmapobject
     * exists and dependant on the non-existant mode
     * @param tmo
     * @param output
     * @throws JellyTagException
     */
    protected void doTag(TopicMapObject tmo, XMLOutput output) throws JellyTagException
    
    {
        
    if (tmo == null) {
        // failed to retrieve topic
        if (shallFailOnNonexistant())
            throw new JellyTagException("Failed to identify topicmap object");

        else if (shallAddOnNonexistant()){
            // topic will be added
            tmo = createTMO();

            // set variable
            storeObject(tmo);
            
            // process body
            getBody().run(context, output);
        }
        else {
            // reset var, ignore body
            storeObject(null);
            return;
        }
    }
    else {
        // Topic did exist
        // set variable
        storeObject(tmo);

        // process body only, if the body was not meant to create a topic
        if(shallIgnoreOnNonexistant()){
            // process body
            getBody().run(context, output);
            
        }
    }
    }
    
    /**
     * Hook to trigger the creation of a topicmap object through
     * an extending class.
     * 
     * This hook is called, if the doTag-Strategy decides that a new
     * topicmap object must be added.
     * @return
     * @throws JellyTagException
     */
    protected abstract TopicMapObject createTMO() throws JellyTagException;
}
