package net.sf.semantag.tm;

/**
 * This interface tags all Tags that
 * are able to reference a TopicMapObject 
 * through a variable.
 * 
 * @author cf
 * @version 0.1, created on 03.09.2004
 */
public interface ReferenceFromVar {

    /**
     * @return the name of a variable to which an association is bound
     */
    public String getFromVar();

    /**
     * sets the name of a variable to which an association is bound
     * 
     * @param topic
     */
    public void setFromVar(String varname);


}