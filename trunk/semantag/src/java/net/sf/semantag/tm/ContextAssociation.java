package net.sf.semantag.tm;

import org.tm4j.topicmap.Association;

/**
 * To be implemented by classes that provide an
 * association to the context of their successors.
 * @author cf
 * @version 0.1, created on 08.09.2004
 */
public interface ContextAssociation {

    public Association getAssociation();
}
