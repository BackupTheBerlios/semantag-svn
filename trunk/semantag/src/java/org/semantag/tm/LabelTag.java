//$Id: LabelTag.java,v 1.6 2004/12/29 21:30:26 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * Prints a label for an object.
 * <br/><br/>
 * This tag is usually used to print a label for a TopicMapObject
 * or for a locator
 * 
 * @jelly
 *  name="label"
 * 
 * @author cf
 */
public class LabelTag extends BaseTMTag {

    /** The Log to which logging calls will be made. */
    private static final Log log = LogFactory.getLog(LabelTag.class);

    /**
     * The object for which the label will be returned.
     */
    private Object object;


    /**
     * Prints the label for an object
     */
    public void doTag(XMLOutput output) throws MissingAttributeException,
            JellyTagException {

        if (log.isDebugEnabled())
            log.debug("Print label for " + object);

        // assert that an object is provided
        validate();

        // get the label
        String label = NameUtil.labelFor(object);
        
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
        if (object == null) {
            String msg = "The label-tag requires that an object is set via";
            msg += "the 'object'-attribute.";
            throw new JellyTagException(msg);

        }
    }

    
    /**
     * The object for which a label will be printed.
     * <br/>
     * This is usually an object of type TopicMapObject or of type Locator.
     * Nevertheless its possible to pass any object.
     * 
     * @jelly
     *   required="yes"
     * 
     * @param tmo the object for which a label shall be returned.
     */
    public void setObject(Object tmo) {
        this.object = tmo;
    }

    
    /**
     * @return the object for which a label shall be returned
     */
    public Object getObject() {
        return object;
    }
    
    /**
     * Overrides the method from the superclass and blocks
     * the setting of a source locator since labels 
     * have no sourceLocators.
     * 
     * @jelly
     *    ignore="true"
     */
      public void setSourceLocator(String sourceLocator) {
      }

      /**
       * Overrides the method from the superclass and blocks
       * the setting of an id since labels have no id
       * 
       * @jelly
       *    ignore="true"
       */

      public void setId(String id) {
      }

}