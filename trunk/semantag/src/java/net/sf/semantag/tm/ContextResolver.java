package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class ContextResolver {

    /**
     *  
     */
    public ContextResolver() {
        super();
    }


    public static TopicMap getTopicMap(TagSupport tag, String varname)
            throws JellyTagException {

        JellyContext ctx = tag.getContext();
        Object o;
        // First check for a topicmap that was 
        // explicitly specified through a variable
        if (isSpecified(varname)) {
            o = ctx.getVariable(varname);
            // if a variable name is specified than every result
            // different from a topicmap counts as an error
            if (o == null || !(o instanceof TopicMap)) {
                throw new JellyTagException("Variable '" + varname
                        + "' is not bound to a Topicmap but to " + o);
            }
        }
        
        else {
            // secondly check for any predecessor that implements
            // TopicMapReference
            ContextTopicMap tmTag = (ContextTopicMap)
                TagSupport.findAncestorWithClass(tag, ContextTopicMap.class);
            if(tmTag != null){
                return tmTag.getTopicMap();
            }
            
            // thirdly check for a default topicmap
            o = ctx.getVariable(Dictionary.KEY_TOPICMAP);
            // if no variable is specified and no default topicmap
            // is stored, the method returns null, rather than throwing
            // an exception
            if (o == null)
                return null;
            if (!(o instanceof TopicMap)) {
                // big, big error
                throw new JellyTagException(
                        "The variable to store the default Topicmap ('"
                                + "') is not bound to a Topicmap but to " + o);
            }

        }
        return (TopicMap) o;

    }

    /**
     * returns a topic that is bound to the variable with the
     * given name.
     * 
     * @param ctx
     * @param varname the name of a variable to which a topic is bound
     * @return a topic or null, if the varname was not given or was not bound to an object
     * @throws JellyTagException if the variable was bound to an object that is not of type topic
     */
    public static Topic getTopicFromVariable(JellyContext ctx, String varname) 
        throws JellyTagException{

        if (isSpecified(varname)) {
            Object o = ctx.getVariable(varname);
            // if a variable name is specified than every result
            // different from a topic counts as an error
            if (o == null || !(o instanceof Topic)) {
                throw new JellyTagException("Variable '" + varname
                        + "' is not bound to a Topic but to " + o);
            }
            return (Topic)o;
        }
        else {
            return null;
        }
        
    }
    
    public static Topic getTopic(TagSupport tag, String varname) throws JellyTagException{
        
        Topic topic = getTopicFromVariable(tag.getContext(), varname);

        if (topic == null && !(isSpecified(varname))) {
            // if varname was not specified, check the context
            ContextTopic ct = (ContextTopic)TagSupport.findAncestorWithClass(tag, ContextTopic.class);
            if(ct != null){
                topic = ct.getTopic();
            }
        }
        return topic;
        
    }
    

    public static Association getAssociation(TagSupport tag, String varname) throws JellyTagException{
        JellyContext ctx = tag.getContext();
        Association assoc = null;

        if (isSpecified(varname)) {
            Object o = ctx.getVariable(varname);
            // if a variable name is specified than every result
            // different from a topic counts as an error
            if (o == null || !(o instanceof Association)) {
                throw new JellyTagException("Variable '" + varname
                        + "' is not bound to a Topic but to " + o);
            }
            assoc = (Association)o;
        }
        else {
        
            ContextAssociation ct = (ContextAssociation)TagSupport.findAncestorWithClass(tag, ContextAssociation.class);
            if(ct != null){
                assoc = ct.getAssociation();
            }
        }
        return assoc;
        
    }
    
    
    private static boolean isSpecified(String varname) {
        if (varname == null)
            return false;
        if (varname.length() == 0)
            return false;
        return true;
    }

}