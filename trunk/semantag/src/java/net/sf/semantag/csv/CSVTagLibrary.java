// $Id: CSVTagLibrary.java,v 1.1 2004/08/24 00:12:27 niko_schmuck Exp $
package net.sf.semantag.csv;

import net.sf.semantag.csv.CSVTag;
import net.sf.semantag.csv.SetTag;

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
public class CSVTagLibrary extends TagLibrary {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(CSVTagLibrary.class);

  public CSVTagLibrary() {
    log.debug("Registering tags for csv tag library.");
    registerTag("csv", CSVTag.class);
    registerTag("set", SetTag.class);
  }
}
