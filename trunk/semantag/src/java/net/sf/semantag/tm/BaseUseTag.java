package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;



/**
 * BaseTags for all Tags that are able to resolve
 * an existing TopicMapObject.
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public abstract class BaseUseTag extends BaseTMTag implements ReferenceFromVar, ReferenceTopicMap{

    public final static String NE_IGNORE_BODY = "ignore";
    public final static String NE_ADD = "add";
    public final static String NE_FAIL = "fail";
    
    // defaults to ignore
    public final static String NE_DEFAULT= NE_IGNORE_BODY;
    
    private String nonexistant = NE_DEFAULT;
    
    protected TMOResolver tmoResolver = new TMOResolver();

    // the name of a variable that holds a topicmap
    private String tmVar;

    
    /**
     * 
     */
    public BaseUseTag() {
        super();
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
        TopicMap tm = getTopicMapFromContext(tmVar);
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
        
    
    protected TopicMap getTopicMapFromContext()
        throws JellyTagException
    {
        return getTopicMapFromContext(tmVar);
    }
    
    public String getId() {
        return tmoResolver.getId();
    }
    public String getSourceLocator() {
        return tmoResolver.getSourceLocator();
    }
    public String getFromVar() {
        return tmoResolver.getVariable();
    }
    /**
     * @return the name of the variable that hold
     * the topicmap to which this tag refers to
     */
    public String getTmVar() {
        return tmVar;
    }


    public void setId(String id) {
        tmoResolver.setId(id);
    }
    public void setSourceLocator(String sourceLocator) {
        tmoResolver.setSourceLocator(sourceLocator);
    }
    public void setFromVar(String varname) {
        tmoResolver.setVariable(varname);
    }
    
    /**
     * sets the name of the variable, that holds
     * the topicmap to which this tag refers to.
     */
    public void setTmVar(String tmVar) {
        this.tmVar = tmVar;
    }

}
