package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Resolves a topic map object that was specified by either id, sourceLocator or
 * by a variable in the jelly context
 * 
 * @author cf
 * @version 0.1, created on 02.09.2004
 *  
 */
public class TMOResolver {

    /**
     * Name of a variable that hold a topic map object
     */
    private String variable;

    /**
     * Id of a topic
     */
    private String id;

    /**
     * Sourcelocator of a topic
     */
    private String sourceLocator;

    private TM4JConnector tmEngine;
   
    /**
     *  
     */
    public TMOResolver(TM4JConnector tmEngine) {
        
        this.tmEngine = tmEngine;
        
    }

    /**
     * Tries to resolve the topic by dereferecing one of the identification
     * properties. The properties are processed in the following order:
     * <ul>
     * <li>variable</li>
     * <li>id</li>
     * <li>sourceLocator</li>
     * </ul>
     * 
     * @param tm
     * @param ctx
     * @return the referenced topic map object or null if no topic map object
     *         could be found
     * @throws JellyTagException
     * @throws LocatorFactoryException
     */
    public TopicMapObject getTopicMapObject(TopicMap tm, JellyContext ctx)
            throws JellyTagException{

        TopicMapObject reference;

        if ((reference = identifyByVariable(ctx)) != null)
            return reference;
        if ((reference = identifyByID(tm)) != null)
            return reference;
        if ((reference = identifyBySourceLocator(tm)) != null)
            return reference;

        return null;
    }

    /**
     * Tries to get the refered topic map object from the variable that is
     * specified in the <code>variable</code> -property. <br>
     * 
     * If no variable is specified, this method simply returns false.
     * 
     * If a variable is specified, but this variable is not bound to any object
     * in the context, the method also returns false
     * 
     * If a variable is specified and bound to an object, but this object is not
     * an instance of TopicMapObject, the method throws a JellyException
     * 
     * Otherwise the method returns the object found
     * 
     * @param ctx
     * @return a topic map object that was identified, null if no object for the
     *         variable specified was found
     * @throws JellyException
     *             if an attempt to identify a topic map object was taken but in
     *             an apparently erroneous manner.
     */
    protected TopicMapObject identifyByVariable(JellyContext ctx)
            throws JellyTagException {

        // name of variable supplied?
        if (variable == null)
            return null;

        Object o = ctx.getVariable(variable);
        String identifier = "Variable " + variable;
        return getReference(o, identifier);

    }

    /**
     * Tries to identify the refered topic map object by the value of the
     * <code>Id</code> -property. <br>
     * 
     * If no Id is specified, this method simply returns false.
     * 
     * If an Id is specified, but does not refer to an Object in the given
     * TopicMap, the method also returns false
     * 
     * Otherwise the method returns the object found
     * 
     * @param tm
     *            the TopicMap to lookup the topic in.
     * @return the found TopicMapObject or null if none was found
     */
    protected TopicMapObject identifyByID(TopicMap tm) {

        // id of a topic supplied?
        if (id == null)
            return null;

        return tm.getObjectByID(id);

    }

    /**
     * Tries to identify the refered topic map object by the value of the
     * <code>sourceLocator</code> -property. <br>
     * 
     * If no sourceLocator is specified, this method simply returns null.
     * 
     * If something was specified as the adress of a sourceLocator, that could
     * not be parsed as a valid locator reference the method throws a
     * LocatorFactoryException
     * 
     * If a sourceLocator is specified, but does not refer to an Object in the
     * given TopicMap, the method also returns false
     * 
     * Otherwise the method returns the object found
     * 
     * @param tm
     *            the TopicMap to lookup the topic in.
     * @return the TopicMapObject identified, null if no TopicMapObject could be found
     * @throws LocatorFactoryException
     *             if an attempt to identify a topic map object was undertaken
     *             but in an apparently erroneous manner.
     */
    protected TopicMapObject identifyBySourceLocator(TopicMap tm)
            throws JellyTagException {

        // no adress specified
        if(sourceLocator == null) return null;
        
        Locator sl = tmEngine.createLocator(sourceLocator, tm);

        // sourceLocator of a topic supplied?
        if (sl == null)
            return null;

        return tm.getObjectBySourceLocator(sl);

    }

    /**
     * Sets the resolved topic reference of this instance to the given object.
     * <br>
     * 
     * If the given object is null, the method returns false.
     * 
     * 
     * If the given object is not an instance of Topic, the method throws a
     * JellyTagException
     * 
     * Otherwise the <code>reference</code> -property of this instance is set
     * to the given topic and the method returns true
     * 
     * @param o
     *            the Topic
     * @param identifier
     *            the identifier from wich the given object results. This param
     *            is solely used to generate the message of an exception.
     * @return true, if <code>o</code> was not null and an instance of Topic,
     *         false otherwise
     * @throws JellyException
     *             if o is not an instance of Topic.
     */
    protected TopicMapObject getReference(Object o, String identifier)
            throws JellyTagException {

        if (o == null)
            return null;

        if (!(o instanceof TopicMapObject)) {
            // the identifier was bound to an object
            // other than a topic.
            // This is a failure
            String msg = identifier
                    + " does not refer to an instace of TopicMapObject but to an ";
            msg += "instance of " + o.getClass();
            throw new JellyTagException(msg);
        }

        // success
        return (TopicMapObject) o;

    }

  

    // ---------------------------------------------
    // Getter/Setter
    //  ---------------------------------------------
    public String getVariable() {
        return variable;
    }

    public String getId() {
        return id;
    }

    public String getSourceLocator() {
        return sourceLocator;
    }

    public void setVariable(String varname) {
        this.variable = varname;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSourceLocator(String sl) {
        this.sourceLocator = sl;
    }
}