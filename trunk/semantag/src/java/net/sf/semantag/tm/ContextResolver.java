package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;
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

    
    public static Topic getTopic(TagSupport tag, String varname) throws JellyTagException{
        JellyContext ctx = tag.getContext();
        Topic topic = null;

        if (isSpecified(varname)) {
            Object o = ctx.getVariable(varname);
            // if a variable name is specified than every result
            // different from a topic counts as an error
            if (o == null || !(o instanceof Topic)) {
                throw new JellyTagException("Variable '" + varname
                        + "' is not bound to a Topic but to " + o);
            }
            topic = (Topic)o;
        }
        
        ContextTopic ct = (ContextTopic)TagSupport.findAncestorWithClass(tag, ContextTopic.class);
        if(ct != null){
            topic = ct.getTopic();
        }
        return topic;
        
    }
    
    
    private static boolean isSpecified(String varname) {
        if (varname == null)
            return false;
        if (varname.length() == 0)
            return false;
        return true;
    }

}