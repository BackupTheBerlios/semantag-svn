// $Id: RDFTagLibrary.java,v 1.2 2004/12/19 20:45:28 c_froehlich Exp $
package org.semantag.rdf;

import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The RDF tag library contains tags that support the querying of RDF triples.
 * @jelly
 *   defNS="xmlns:rdf=\"jelly:org.semantag.rdf.RDFTagLibrary\""
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
