package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.JellyTagException;
import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

/**
 * Resolves a topic that was specified in a way
 * defined by the TopicReference
 * 
 * @author cf
 * @version 0.1, created on 02.09.2004
 * 
 */
//TODO add topic resolving by name
public class TopicResolver implements TopicReference{

    /**
     * The topic that is referenced by this class
     */
    private Topic reference = null;

    /**
     * Name of a variable that hold a topic
     */
    private String topicVariable;

    /**
     * Id of a topic
     */
    private String topicId;

    /**
     * Sourcelocator of a topic
     */
    private String topicSourceLocator;

    /**
     * Addressvalue of a SourceLocator of a topic
     */
    private String topicSubjectIndicator;

    /**
     * Subject-Locator of a topic
     */
    private String topicSubject;

    /**
     * Namevalue of a topic
     */
    private String topicName;

    /**
     * The notation that this resolver uses to 
     * let Locators be created.
     */
    private String LOCATOR_NOTATION = "URI";

    /**
     *  
     */
    public TopicResolver() {
    }

    /**
     * Tries to resolve the topic by dereferecing
     * one of the identification properties. The 
     * properties are processed in the following order:
     * <ul>
     * <li>variable</li>
     * <li>topicID</li>
     * <li>sourceLocator</li>
     * <li>subject</li>
     * <li>subjectIndicator</li>
     * <li>name (not implemented yet)</li>
     * @param tm
     * @param ctx
     * @return the referenced topic or null if no topic
     * could be found
     * @throws JellyTagException
     * @throws LocatorFactoryException
     */
    public Topic getTopic(TopicMap tm, JellyContext ctx)
            throws JellyTagException, LocatorFactoryException {

        if (identifyTopicByVariable(ctx))
            return reference;
        if (identifyTopicByID(tm))
            return reference;
        if (identifyTopicBySourceLocator(tm))
            return reference;
        if (identifyTopicBySubject(tm))
            return reference;
        if (identifyTopicBySubjectIndicator(tm))
            return reference;
        
        return null;
    }

    /**
     * Tries to get the refered topic from the variable that is specified in the
     * <code>topic</code> -property. <br>
     * 
     * If no variable is specified, this method simply returns false.
     * 
     * If a variable is specified, but this variable is not bound to any object
     * in the context, the method also returns false
     * 
     * If a variable is specified and bound to an object, but this object is not
     * an instance of Topic, the method throws a JellyException
     * 
     * Otherwise the <code>reference</code> -property of this instance is set
     * to the found topic and the methos returns true
     * 
     * @param ctx
     * @return true, if a topic could be identified, false otherwise
     * @throws JellyException
     *             if an attempt to identify a topic was taken but in an
     *             apparently erroneous manner.
     */
    protected boolean identifyTopicByVariable(JellyContext ctx)
            throws JellyTagException {

        // name of variable supplied?
        if (topicVariable == null)
            return false;


        Object o = ctx.getVariable(topicVariable);
        String identifier = "Variable "+topicVariable;
        return setReference(o, identifier);

    }

    /**
     * Tries to identify the refered topic 
     * by the value of the 
     * <code>topicId</code> -property. <br>
     * 
     * If no topicId is specified, this method simply returns false.
     * 
     * If a topicId is specified, but does not refer to an Object
     * in the given TopicMap, the method also returns false
     * 
     * If a topicId is specified and this id refers to an object in the 
     * given TopicMap that is not an instance of Topic,
     * the method throws a JellyException
     * 
     * Otherwise the <code>reference</code> -property of this instance is set
     * to the found topic and the method returns true
     * 
     * @param tm the TopicMap to lookup the topic in.
     * @return true, if a topic could be identified, false otherwise
     * @throws JellyException
     *             if an attempt to identify a topic was undertaken but in an
     *             apparently erroneous manner.
     */
    protected boolean identifyTopicByID(TopicMap tm) 
        throws JellyTagException
    {

        // id of a topic supplied?
        if (topicId == null)
            return false;

        TopicMapObject tmo = tm.getObjectByID(topicId);
        String identifier = "ID "+topicId;
        return setReference(tmo, identifier);
        
    }

    
    /**
     * Tries to identify the refered topic 
     * by the value of the 
     * <code>sourceLocator</code> -property. <br>
     * 
     * If no sourceLocator is specified, this method simply returns false.
     * 
     * If something was specified as the adress of a sourceLocator, that
     * could not be parsed as a valid locator reference the method throws
     * a LocatorFactoryException
     * 
     * If a sourceLocator is specified, but does not refer to an Object
     * in the given TopicMap, the method also returns false
     * 
     * If a sourceLocator is specified and refers to an object in the 
     * given TopicMap that is not an instance of Topic,
     * the method throws a JellyTagException
     * 
     * Otherwise the <code>reference</code> -property of this instance is set
     * to the found topic and the method returns true
     * 
     * @param tm the TopicMap to lookup the topic in.
     * @return true, if a topic could be identified, false otherwise
     * @throws LocatorFactoryException
     *             if an attempt to identify a topic was undertaken but in an
     *             apparently erroneous manner.
     * @throws JellyException
     *             if an attempt to identify a topic was undertaken but in an
     *             apparently erroneous manner.
     */
    protected boolean identifyTopicBySourceLocator(TopicMap tm) 
    throws JellyTagException, LocatorFactoryException
{


    Locator sl = parseAdress(tm, topicSourceLocator);
    
    // sourceLocator of a topic supplied?
    if (sl == null)
        return false;
    
    TopicMapObject tmo = tm.getObjectBySourceLocator(sl);
    String identifier = "SourceLocator "+topicSourceLocator;
    return setReference(tmo, identifier);
    
    }
    
    
    /**
     * Tries to identify the refered topic 
     * by the value of the 
     * <code>subject</code> -property. <br>
     * 
     * If no subject is specified, this method simply returns false.
     * 
     * If something was specified as the adress of a subject, that
     * could not be parsed as a valid locator reference the method throws
     * a LocatorFactoryException
     * 
     * If a subject is specified, but does not refer to a Topic
     * in the given TopicMap, the method also returns false
     * 
     * Otherwise the <code>reference</code> -property of this instance is set
     * to the found topic and the method returns true
     * 
     * @param tm the TopicMap to lookup the topic in.
     * @return true, if a topic could be identified, false otherwise
     * @throws LocatorFactoryException
     *             if an attempt to identify a topic was undertaken but in an
     *             apparently erroneous manner.
     * @throws JellyException
     *             if an attempt to identify a topic was undertaken but in an
     *             apparently erroneous manner.
     */
    protected boolean identifyTopicBySubject(TopicMap tm) 
    throws JellyTagException, LocatorFactoryException
{

    // subjectLocator of a topic supplied?
        Locator su = parseAdress(tm, topicSubject);        
    if (su == null)
        return false;

    Topic o = tm.getTopicBySubject(su);
    String identifier = "SubjectLocator "+topicSubject;
    return setReference(o, identifier);
    
    }
    
    /**
     * Tries to identify the refered topic 
     * by the value of the 
     * <code>subjectIndicator</code> -property. <br>
     * 
     * If no subjectIndicator is specified, this method simply returns false.
     * 
     * If something was specified as the adress of a subjectIndicator, that
     * could not be parsed as a valid locator reference the method throws
     * a LocatorFactoryException
     * 
     * If a subjectIndicator is specified, but does not refer to a Topic
     * in the given TopicMap, the method also returns false
     * 
     * Otherwise the <code>reference</code> -property of this instance is set
     * to the found topic and the method returns true
     * 
     * @param tm the TopicMap to lookup the topic in.
     * @return true, if a topic could be identified, false otherwise
     * @throws LocatorFactoryException
     *             if an attempt to identify a topic was undertaken but in an
     *             apparently erroneous manner.
     * @throws JellyException
     *             if an attempt to identify a topic was undertaken but in an
     *             apparently erroneous manner.
     */
    protected boolean identifyTopicBySubjectIndicator(TopicMap tm) 
    throws JellyTagException, LocatorFactoryException
{

        Locator sl = parseAdress(tm, topicSubjectIndicator);
    // subjectLocator of a topic supplied?
    if (sl == null)
        return false;

    Topic o = tm.getTopicBySubjectIndicator(sl);
    String identifier = "SubjectIndicator "+topicSubjectIndicator;
    return setReference(o, identifier);
    
    }
    
    
        
    /**
     * Sets the resolved topic reference of this 
     * instance to the given object. <br>
     * 
     * If the given object is null, the method returns false.
     * 
     * 
     * If the given object is not an instance of Topic,
     * the method throws a JellyTagException
     * 
     * Otherwise the <code>reference</code> -property of this instance is set
     * to the given topic and the method returns true
     * 
     * @param o the Topic
     * @param identifier the identifier from wich the given object results.
     * This param is solely used to generate the message of an exception.
     * @return true, if <code>o</code> was not null and an instance of Topic, 
     * false otherwise
     * @throws JellyException
     *             if o is not an instance of Topic.
     */
    private boolean setReference(Object o, String identifier) throws JellyTagException{

        if(o == null) return false;
        
        if (!(o instanceof Topic)) {
            // the identifier was bound to an object
            // other than a topic.
            // This is a failure
            String msg = identifier
                    + " does not refer to an instace of Topic but to an ";
            msg += "instance of " + o.getClass();
            throw new JellyTagException(msg);
        }

        // success
        reference = (Topic) o;
        return true;

    }
    
    
    private Locator parseAdress(TopicMap tm, String address) 
        throws LocatorFactoryException
    {
        
        if(address == null) return null;
        
        return tm.getLocatorFactory().createLocator(LOCATOR_NOTATION, address);
    }
    
    public String getTopicVar() {
        return topicVariable;
    }
    
    public String getTopicID() {
        return topicId;
    }
    public String getTopicName() {
        return topicName;
    }
    public String getTopicSubjectIndicator() {
        return topicSubjectIndicator;
    }
    public String getTopicSourceLocator() {
        return topicSourceLocator;
    }
    public String getTopicSubject() {
        return topicSubject;
    }

    public void setTopicVar(String topic) {
        this.topicVariable = topic;
    }
    public void setTopicID(String topicID) {
        this.topicId = topicID;
    }
    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
    public void setTopicSubjectIndicator(String topicSI) {
        this.topicSubjectIndicator = topicSI;
    }
    public void setTopicSourceLocator(String topicSL) {
        this.topicSourceLocator = topicSL;

    }
    public void setTopicSubject(String topicSubject) {
        this.topicSubject = topicSubject;
    }
}