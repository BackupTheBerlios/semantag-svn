// $Id: RDFTagLibrary.java,v 1.1 2004/10/26 19:50:37 niko_schmuck Exp $
package org.semantag.rdf;

import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The tag library basically registers all jelly tags,
 * so that they can be accessed from a jelly script under a specific
 * namespace.
 *
 * @author cf
 */
public class RDFTagLibrary extends TagLibrary {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(RDFTagLibrary.class);

  public RDFTagLibrary() {
    log.debug("Registering tags for rdf tag library.");
    registerTag("rdf", RDFTag.class);
    registerTag("object", ObjectTag.class);
  }
}
