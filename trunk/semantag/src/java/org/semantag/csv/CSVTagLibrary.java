// $Id: CSVTagLibrary.java,v 1.2 2004/11/22 09:45:31 c_froehlich Exp $
package org.semantag.csv;


import org.apache.commons.jelly.TagLibrary;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semantag.csv.CSVTag;
import org.semantag.csv.SetTag;

/**
 * The tag library basically registers all jelly tags,
 * so that they can be accessed from a jelly script under a specific
 * namespace.
 *
 * @author cf
 */
public class CSVTagLibrary extends TagLibrary {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(CSVTagLibrary.class);

  public CSVTagLibrary() {
    log.debug("Registering tags for csv tag library.");
    registerTag("csv", CSVTag.class);
    registerTag("set", SetTag.class);
    registerTag("header", HeaderTag.class);
  }
}
