package net.sf.semantag.tm;

/**
 * Every TopicMapObject is referenceable
 * by id and by sourceLocator
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public interface ReferenceTopicMapObject {

    public String getId();
    public void setId(String id);
    public String getSourceLocator();
    public void setSourceLocator(String sourceLocator);
}
