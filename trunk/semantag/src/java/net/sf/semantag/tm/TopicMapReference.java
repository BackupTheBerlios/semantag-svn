package net.sf.semantag.tm;

/**
 * This interface contains common semantics
 * to reference a topicMap.
 * 
 * @author cf
 * @version 0.1, created on 03.09.2004
 */
public interface TopicMapReference {

    /**
     * @return the name of a variable to which a topicmap is bound
     */
    public String getTmVar();

    /**
     * sets the name of a variable to which a topicmap is bound
     * 
     * @param topic
     */
    public void setTmVar(String topic);


}