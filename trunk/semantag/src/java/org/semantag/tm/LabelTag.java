//$Id: LabelTag.java,v 1.2 2004/11/29 06:38:13 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * A tag that returns a label for an object.
 * This tag is usually used to get a label for a topic map object
 * or a locator
 * 
 * @author cf
 */
public class LabelTag extends BaseTMTag {

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(LabelTag.class);

    /**
     * The object for which the label will be returned.
     */
    private Object tmo;


    /**
     * Prints the label for an object
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if (log.isDebugEnabled())
            log.debug("Print label for " + tmo);

        // assert that an object is provided
        validate();

        // get the label
        String label = NameUtil.labelFor(tmo);
        
        // print it
        try {
            output.write(label);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new JellyTagException(e);
        }
    }

    /**
     * does nothing, since this tag does not rely on any context
     */
    protected void validateContext() throws JellyTagException {
    }

    
    /**
     * ensures that an object is set
     */
    protected void validate() throws JellyTagException {
        if (tmo == null) {
            String msg = "The label-tag requires that a topic map object is set via";
            msg += "the 'tmo'-attribute.";
            throw new JellyTagException(msg);

        }
    }

    
    /**
     * @param tmo the object for which a label shall be returned.
     * This is usually an object of type TopicMapObject or of type Locator.
     * Nevertheless its possible to pass any object.
     */
    public void setTmo(Object tmo) {
        this.tmo = tmo;
    }

    
    /**
     * @return the object for which a label shall be returned
     */
    public Object getTmo() {
        return tmo;
    }
}