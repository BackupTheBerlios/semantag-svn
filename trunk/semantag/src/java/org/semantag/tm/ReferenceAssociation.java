package org.semantag.tm;

/**
 * This interface contains common semantics
 * to reference a topicMap.
 * 
 * @author cf
 * @version 0.1, created on 03.09.2004
 */
public interface ReferenceAssociation {

    /**
     * @return the name of a variable to which an association is bound
     */
    public String getAssocVar();

    /**
     * sets the name of a variable to which an association is bound
     * 
     * @param topic
     */
    public void setAssocVar(String varname);


}