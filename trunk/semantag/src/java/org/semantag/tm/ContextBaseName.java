package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.tm4j.topicmap.BaseName;

/**
 * To be implemented by classes that provide a
 * basename to the context of their successors.
 * @author cf
 * @version 0.1, created on 08.09.2004
 */
public interface ContextBaseName {

    public BaseName getBaseName() throws JellyTagException;
}
