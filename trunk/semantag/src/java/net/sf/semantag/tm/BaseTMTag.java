package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.logging.Log;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactory;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Member;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

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
    
    protected TM4JConnector tmEngine;

    /**
     *  
     */
    public BaseTMTag() {
        super();
        tmEngine = new TM4JConnector();
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
        if(var != null) {
           if(o != null) context.setVariable(var, o);
           else context.removeVariable(var);

        }
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
    public TopicMap getTopicMapFromContext(String varname) throws JellyTagException{
        return ContextResolver.getTopicMap(this, varname);
    }
    
    /**
     * Asks the contextResolver to return 
     * a topic from the context for this tag
     * 
     * @return 
     * @throws JellyTagException
     * @deprecated use getTopicFromContext() instead
     */
    public Topic getTopicFromContext(String varname) throws JellyTagException{
        return ContextResolver.getTopic(this, varname);
    }
    
    /**
     * Asks the contextResolver to return 
     * a topic from the context for this tag
     * 
     * @return 
     * @throws JellyTagException
     */
    public Topic getTopicFromContext() throws JellyTagException{
        return ContextResolver.getTopic(this, null);
    }
    

    /**
     * Asks the contextResolver to return 
     * a topic from the context for this tag
     * 
     * @return 
     * @throws JellyTagException
     */
    public BaseName getBasenameFromContext() throws JellyTagException{
        return ContextResolver.getBasename(this);
    }
    
    
    /**
     * Asks the contextResolver to return 
     * a topic from the variable with the given name
     * @param varname the name of the variable
     * @return topic that is bound to the given variable in
     * the context of this tag
     * @throws JellyTagException
     */
    public Topic getTopicFromVariable(String varname) throws JellyTagException{
        return ContextResolver.getTopic(this, varname);
    }
    
    /**
     * Asks the contextResolver to return 
     * an association from the context for this tag
     * @return 
     * @throws JellyTagException
     */
    public Association getAssociationFromContext() throws JellyTagException{
        return ContextResolver.getAssociation(this);
    }
    
    /**
     * Asks the contextResolver to return 
     * an Occurrence from the context for this tag
     * @return 
     * @throws JellyTagException
     */
    public Occurrence getOccurrenceFromContext() throws JellyTagException{
        return ContextResolver.getOccurrence(this);
    }
    
    /**
     * Asks the contextResolver to return 
     * a Member from the context for this tag
     * @return 
     * @throws JellyTagException
     */
    public Member getMemberFromContext() throws JellyTagException{
        return ContextResolver.getMember(this);
    }

    /**
     * Asks the contextResolver to return 
     * a TopicMapObject for which a type can be set
     * from the context of the given tag
     * @return 
     * @throws JellyTagException
     */
    public TopicMapObject getTypeableFromContext() throws JellyTagException{
        return ContextResolver.getTypeableObject(this);
    }

    /**
     * Asks the contextResolver to return 
     * a TopicMapObject that can be scoped
     * from the context of the given tag
     * @return 
     * @throws JellyTagException
     */
    public TopicMapObject getScopeableFromContext() throws JellyTagException{
        return ContextResolver.getTypeableObject(this);
    }

    /**
     * Helper that creates a Locator for the given address.
     * The Locator is created with the "URI" notation scheme.
     * 
     * If no factory is passed in, the method tries to get 
     * the topicmap from the current context and uses the 
     * LocatorFactory supplied by this topicmap. 
     * 
     * If no topicmap for the current context could be found
     * a JellyTagException is thrown
     * 
     * @param adress the adress of the Locator to be created
     * @param factory a LocatorFactory that will be used to create
     * the Locator. May be null, if currently a topicmap is available
     * in the context of this tag
     * @param log instance of Log where debug-logs get written too. May be null.
     * @return the Locator created
     * @throws JellyTagException
     */
    protected Locator createLocator(String adress, LocatorFactory factory, Log log ) throws JellyTagException {
      if (log != null && log.isDebugEnabled()) {
        log.debug("Create baseLocator for String: " + adress);
      }

      try {
        if(factory == null){
            // if a topicmap is currently in context,
            // we use the factory that it supplies.
            TopicMap tm = getTopicMapFromContext(null);
            if(tm == null){
                String msg = "Unable to get a LocatorFactory to create ";
                msg = "locator with adress "+adress;
                throw new JellyTagException(msg);
            }
            factory = tm.getLocatorFactory();
        }
        return factory.createLocator("URI", adress);
      } catch (LocatorFactoryException e) {
        throw new JellyTagException(e);
      }
    }
    
    /**
     * Checks whether data has some non-whitespace
     * characters in it
     * @param data
     * @return true if data is not null and if it
     * contains at least one character that
     * is not whitespace, false otherwise
     */
    protected boolean isSpecified(String data) {
      if ((data == null) || (data.length() == 0) || (data.trim().length() == 0)) {
        return false;
      } else {
        return true;
      }
    }
}