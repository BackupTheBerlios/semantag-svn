package net.sf.semantag.csv;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;

/**
 *
 * @author cf
 * @version 0.1, created on 01.08.2004
 */
public class SetTag extends TagSupport {
  /** The Log to which logging calls will be made. */

  //private static final Log log = LogFactory.getLog(TagSupport.class);

  /**
   * Name of the variable to hold the value
   */
  private String var;

  /**
   * Index of the cell to get the value from
   */
  private int ix = -1;

  /**
   *
   */
  public SetTag() {
    super();
  }

  protected void validate() throws MissingAttributeException {
    if (var == null) {
      throw new MissingAttributeException("Attribute 'var' must be specified");
    }

    if (ix < 0) {
      throw new MissingAttributeException("Attribute 'ix' must be specified and greater than or equal to zero.");
    }
  }

  /* (non-Javadoc)
   * @see org.apache.commons.jelly.Tag#doTag(org.apache.commons.jelly.XMLOutput)
   */
  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    validate();

    // get the parent
    CSVTag parent = getCSVTag();

    if (parent == null) {
      throw new JellyTagException("'getCell' must be a descendant of 'csv'");
    }

    String value = parent.getCell(ix);

    if (value != null) {
      value = value.trim();
    }

    context.setVariable(var, value);
  }

  /**
   * @return
   */
  private CSVTag getCSVTag() {
    return (CSVTag) findAncestorWithClass(CSVTag.class);
  }

  /**
   * @return Returns the ix.
   */
  public int getIx() {
    return ix;
  }

  /**
   * @param ix The ix to set.
   */
  public void setIx(int ix) {
    this.ix = ix;
  }

  /**
   * @return Returns the var.
   */
  public String getVar() {
    return var;
  }

  /**
   * @param var The var to set.
   */
  public void setVar(String var) {
    this.var = var;
  }
}
