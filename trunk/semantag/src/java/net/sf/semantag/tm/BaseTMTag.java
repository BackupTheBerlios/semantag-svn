package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;
import org.tm4j.topicmap.TopicMap;

/**
 * Base class for all topic map jelly tags.
 *
 * @author Niko Schmuck
 * @author cf
 * @version 0.2, created on 06.09.2004
 */
public abstract class BaseTMTag extends TagSupport implements ReferenceTopicMapObject {

    private String id;

    private String sourceLocator;

    private String var;

    /**
     *  
     */
    public BaseTMTag() {
        super();
    }

    
    /**
     * Turning a given string into a normalized one, with most special
     * characters removed.
     */
    protected static String normalize(final String source) {
        if(source == null) return null;
      char[] string = source.toCharArray();
      char[] target = new char[string.length];
      int pos = 0;

      for (int i = 0; i < string.length; i++) {
        switch (string[i]) {
        case '.':
          target[pos++] = '-';

          break;

        case '[':
        case ']':
          target[pos++] = '_';

          break;

        case ' ':
        case '\t':
        case '\n':
        case '\r':
        case '{':
        case '}':
        case '+':
        case '*':
        case '\'':
        case '"':
          break;

        default:
          target[pos++] = string[i];
        }
      }

      return new String(target, 0, pos);
    }

    /**
     * Stores object o in the context.
     * o is stored under the key that is defined in
     * the field var of this class.
     * if var is null, o is not stored.
     *  
     * @param o
     */
    protected void storeObject(Object o){
        if(var != null) context.setVariable(var, o);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = normalize(id);
    }

    public String getSourceLocator() {
        return sourceLocator;
    }

    public void setSourceLocator(String sourceLocator) {
        this.sourceLocator = sourceLocator;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
    
    /**
     * Asks the contextResolver to return 
     * a topicMap from the context for this tag
     * @return
     * @throws JellyTagException
     */
    public TopicMap getTopicMap(String varname) throws JellyTagException{
        return ContextResolver.getTopicMap(this, varname);
    }
}