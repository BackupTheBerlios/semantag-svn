package net.sf.semantag.tm;

/**
 * This interface contains common semantics
 * to reference a topic.
 * 
 * @author cf
 * @version 0.1, created on 03.09.2004
 */
public interface ReferenceTopic {

    /**
     * @return the name of a variable to which a topic is bound
     */
    // public String getTopicVar();

    /**
     * sets the name of a variable to which a topic is bound
     * 
     * @param topic
     */
    // public void setTopicVar(String topic);

    /**
     * @return the id of a topic or null if no topic id was set
     */
    public String getTopicID();

    /**
     * sets the id of a topic
     * 
     * @param topicID
     */
    public void setTopicID(String topicID);

    /**
     * @return the name of a topic or null if no topic name was set
     */
    public String getTopicName();

    /**
     * sets the name of a topic
     * 
     * @param topicName
     */
    public void setTopicName(String topicName);

    /**
     * @return the adress value of a subject indicator or null if no subject
     *         indicator was set
     */
    public String getTopicSubjectIndicator();

    /**
     * sets the address value of a subject indicator
     * 
     * @param topicSI
     */
    public void setTopicSubjectIndicator(String topicSI);

    /**
     * @return the adress value of a sourceLocator or null if no sourceLocator
     *         was set
     */
    public String getTopicSourceLocator();

    /**
     * sets the address value of a sourceLocator
     * 
     * @param topicSourceLocator
     */
    public void setTopicSourceLocator(String topicSL);

    /**
     * @return the adress value of a topics subject or null if no subject was
     *         set
     */
    public String getTopicSubject();

    /**
     * sets the address value of a subject
     * 
     * @param topicSubject
     */
    public void setTopicSubject(String topicSubject);

}