package org.semantag.csv;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

import java.util.HashMap;
import java.util.List;

/**
 *
 * @author cf
 * @version 0.1, created on 01.08.2004
 */
public class CSVTag extends TagSupport {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(TagSupport.class);
  private String file = null;
  private List row;
  private String cellDelimiter = null;
  private String commentIndicator = null;
  private boolean firstLineContainsHeaders = false;
  private boolean lazyArrayAccess = true;
  private String encoding = null;

  /**
   *
   */
  public CSVTag() {
    super();
  }

  /* (non-Javadoc)
   * @see org.apache.commons.jelly.Tag#doTag(org.apache.commons.jelly.XMLOutput)
   */
  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    CSVFile csvfile;

    try {
      csvfile = new CSVFile(file);
    } catch (IOException e) {
      throw new JellyTagException(e);
    }

    csvfile.setFirstLineContainsHeaders(firstLineContainsHeaders);

    if (cellDelimiter != null) {
      csvfile.setCellDelimiter(cellDelimiter);
    }

    if (commentIndicator != null) {
      csvfile.setCommentIndicator(commentIndicator);
    }

    csvfile.setEncoding(encoding);

    try {
      csvfile.open();

      String[] header = csvfile.getColumnLabels();
      HashMap vars = new HashMap();
      int min;

      while (csvfile.hasNext()) {
        // read current row
        row = csvfile.getNext();

        if (csvfile.isFirstLineContainsHeaders()) {
          // export rowdata into variables
          // that have the name of the column header
          min = (header.length < row.size()) ? header.length : row.size();

          for (int i = 0; i < min; i++) {
            vars.put(header[i], row.get(i));
          }

          context.setVariables(vars);
        }

        invokeBody(output);
      }

      row = null;
    } catch (IOException e) {
      throw new JellyTagException(e);
    } finally {
      try {
        csvfile.close();
      } catch (IOException e1) {
        // this does not really change things
        // so log it and proceed
        log.error(e1);
      }
    }
  }

  /**
   * @return Returns the file.
   */
  public String getFile() {
    return file;
  }

  /**
   * @param file The file to set.
   */
  public void setFile(String file) {
    log.info("Set file to " + file);
    this.file = file;
  }

  /**
   * Returns the value of the cell ix of the
   * current row.
   * @param ix the index of the cell to get the value of
   * @return
   * @throws Exception if currently no row is set
   * or if the given cellindex is out of bounds
   */
  public String getCell(int ix) throws JellyTagException {
    if (row == null) {
      throw new JellyTagException("No row set.");
    }

    if ((ix < 0) || (ix >= row.size())) {
      if (isLazyArrayAccess()) {
        return "";
      } else {
        throw new ArrayIndexOutOfBoundsException("Index " + ix +
                                                 " is out of the bounds of the current row");
      }
    }

    return (String) row.get(ix);
  }

  /**
   * @return Returns the cellDelimiter.
   */
  public String getCellDelimiter() {
    return cellDelimiter;
  }

  /**
   * @param cellDelimiter The cellDelimiter to set.
   */
  public void setCellDelimiter(String cellDelimiter) {
    this.cellDelimiter = cellDelimiter;
  }

  /**
   * @return Returns the commentIndicator.
   */
  public String getCommentIndicator() {
    return commentIndicator;
  }

  /**
   * @param commentIndicator The commentIndicator to set.
   */
  public void setCommentIndicator(String commentIndicator) {
    this.commentIndicator = commentIndicator;
  }

  /**
   * @return Returns the firstLineContainsHeaders.
   */
  public boolean isFirstLineContainsHeaders() {
    return firstLineContainsHeaders;
  }

  /**
   * @param firstLineContainsHeaders The firstLineContainsHeaders to set.
   */
  public void setFirstLineContainsHeaders(boolean firstLineContainsHeaders) {
    this.firstLineContainsHeaders = firstLineContainsHeaders;
  }

  public String getEncoding() {
    return encoding;
  }

  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  public boolean isLazyArrayAccess() {
    return lazyArrayAccess;
  }

  public void setLazyArrayAccess(boolean lazyArrayAccess) {
    this.lazyArrayAccess = lazyArrayAccess;
  }
}
