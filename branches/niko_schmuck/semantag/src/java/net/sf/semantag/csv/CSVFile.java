package net.sf.semantag.csv;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;

import java.util.*;

/**
 * CSVFile let the caller read a csv-file an iterate over its rows.. <br/>It
 * reads data from a delimiter separated Textfile und returns the values line
 * per line. <br/>The class expects that the first line of the File contains
 * the column-labels
 */
public class CSVFile {
  // Transfer column-labels?
  public final static String TRANSFORM_COLS_LOWER = "lower";
  public final static String TRANSFORM_COLS_UPPER = "upper";

  // Number of rows that the buffer can store
  public final static int DEFAULT_CAPACITY = 800;
  Log log = LogFactory.getLog(this.getClass());

  // Shall column-lables be transferred when read in
  private String transformColumnLabels = "";

  // The delimiter that separates cell values
  private String cellDelimiter = "\t";

  // The String that is used to indicate that
  // a line is meant as a comment.
  private String commentIndicator = "#";

  // The encoding of the file
  // defaults to the default encoding of the vm
  private String encoding = null;

  // buffer to hold lines from the input file
  private String[] buffer = null;
  private int curBufferSize = 0; // how much entries of the buffer are
                                 // actually filled
  private int cursor = 0; // current position in the buffer

  // Array to store the column-labels
  private String[] columns;

  //private int columnCount = 0;
  // Array to store the tokens of each line
  private ArrayList lineTokens = new ArrayList();

  // The file
  private final File fileToRead;
  private BufferedReader inReader = null;
  private int bufferCapacity = DEFAULT_CAPACITY;

  /**
   * Flag whether the first line contains ColumnHeaders
   */
  private boolean firstLineContainsHeaders = true;

  /**
   * Flag, whether the single cell values shall be trimmed
   *
   */
  private boolean shallTrimValues = false;

  /**
   * The Constructor creates a CSVFile-Instance with the following defaults: -
   * celldelimiter is TAB (\t) - bufferCapacity is 800 Lines - columnlabels
   * will not be transformed, neithter to lower nor to upper case.
   */
  public CSVFile(String filename) throws IOException {
    this(new File(filename));
  }

  public CSVFile(File file) throws IOException {
    fileToRead = file;

    if (!fileToRead.isFile()) {
      throw new IOException(fileToRead.getAbsolutePath() +
                            " is not a normal file.");
    }

    if (!fileToRead.canRead()) {
      throw new IOException("Can't read from file " +
                            fileToRead.getAbsolutePath());
    }
  }

  //  --------------------------------------------------------------------------------------------------
  //  - Object -Setup
  //  --------------------------------------------------------------------------------------------------

  /**
   * sets the cellDelimiter
   *
   * @param delim
   *            the new delimiter. Is ignored if null or an empty String is
   *            passed
   */
  public void setCellDelimiter(String delim) {
    if ((delim != null) && (delim.length() > 0)) {
      cellDelimiter = delim;
    }
  }

  /** @return the cellDelimiter */
  public String getCellDelimiter() {
    return cellDelimiter;
  }

  /**
   * Sets the capacity of the in-buffer. The units are rows. Values lower than
   * 1 will be substituted with the default value
   *
   * @param capacity
   *            the number of lines that the in-buffer will read in a single
   *            pass
   */
  public void setBufferCapacity(int capacity) {
    if (capacity < 1) {
      bufferCapacity = DEFAULT_CAPACITY;
    } else {
      bufferCapacity = capacity;
    }
  }

  /** @return the capacity of the in-buffer */
  public int getBufferCapacity() {
    return bufferCapacity;
  }

  /** Convert column labels to upper or lower case */
  public void setTransformColumnLabels(String transform) {
    if (transform == null) {
      transformColumnLabels = "";
    } else {
      transformColumnLabels = transform;
    }
  }

  /** Convert column labels to upper or lower case */
  public String getTransformColumnLabels() {
    return transformColumnLabels;
  }

  /**
   * @return Returns whether it is expected that the first line contains
   *         column headers.
   */
  public boolean isFirstLineContainsHeaders() {
    return firstLineContainsHeaders;
  }

  /**
   * @sets whether it shall be expected that the first line contains column
   *       headers.
   */
  public void setFirstLineContainsHeaders(boolean firstLineContainsHeaders) {
    this.firstLineContainsHeaders = firstLineContainsHeaders;
  }

  /**
   * @return Returns the commentIndicator.
   */
  public String getCommentIndicator() {
    return commentIndicator;
  }

  /**
   * Sets the String that is used to mark a line as a comment rather than as
   * line of data.
   *
   * Set this to null, if you dont want any lines to be treated as columns
   *
   * @param commentIndicator
   *            The commentIndicator to set.
   */
  public void setCommentIndicator(String commentIndicator) {
    this.commentIndicator = commentIndicator;
  }

  /**
   * @return Returns the shallTrimValues.
   */
  public boolean isShallTrimValues() {
    return shallTrimValues;
  }

  /**
   * Sets whether the values of the single cells should be trimmed before
   * being returned.
   *
   * @param shallTrimValues
   *            The shallTrimValues to set.
   */
  public void setShallTrimValues(boolean shallTrimValues) {
    this.shallTrimValues = shallTrimValues;
  }

  /**
   * Checks if there is at least one row left after the current <br/>Calls to
   * this method does not increment the row pointer.
   */
  public boolean hasNext() throws IOException {
    if (cursor < curBufferSize) {
      return true;
    }

    readBuffer();

    if (cursor < curBufferSize) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * returns an array with the values for the current row. The array-object
   * used is always the same object - in order to minimize memory-overhead. So
   * don't rely on the values in the array after a call next call to this
   * method
   *
   * @return an Array of Strings
   * @throws NoSuchElementException
   *             if no more row is left
   */
  public ArrayList getNext() throws NoSuchElementException {
    if (cursor > curBufferSize) {
      throw new NoSuchElementException("No More Elements in Datasource");
    }

    tokenizeLine(buffer[cursor]);
    cursor++;

    return lineTokens;
  }

  /**
   * Attempt to open the file, extract the column labels and read the first
   * set of lines into the buffer
   */
  public void open() throws IOException {
    log.debug("Start Fetching");

    if (encoding != null) {
      inReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToRead),
                                                          encoding));
    } else {
      // attempt to open the file
      inReader = new BufferedReader(new FileReader(fileToRead));
    }

    // fills the buffer
    readBuffer();

    if (firstLineContainsHeaders) {
      // read columns from first line
      readColumns();

      // move the cursor to second line
      cursor = 1;

      log.debug("Found " + columns.length + " columns");
    }
  }

  /**
   * Closes the InputStream if it is open
   */
  public void close() throws IOException {
    closeInputStream();
  }

  /*
   * (non-Javadoc)
   *
   * @see org.tm4j.harvest.data.MatrixDataProvider#getColumnLabels()
   */
  public String[] getColumnLabels() {
    return columns;
  }

  // --------------------------------------------------------------------------------------------------
  // - private methods
  // --------------------------------------------------------------------------------------------------
  // Is called after the buffer is filled the first time.<p>
  //  Stores the column-lables in the columns-Array.<p>
  // Transforms the columnlables to upper or to lower case
  // (if specified)
  // throws an IOException if no column-labels could be read in
  // This is important since the length of the columns-Array is used as
  // the size of the matrix.
  private void readColumns() throws IOException {
    tokenizeLine(buffer[0]);

    String[] cols = new String[lineTokens.size()];

    cols = (String[]) lineTokens.toArray(cols);

    //(buffer[0], null);
    if ((cols == null) || (cols.length == 0)) {
      throw new IOException("Datasource contains no data.");
    }

    columns = cols;

    int columnCount = columns.length;

    // If requested, transform column labels
    if (transformColumnLabels.equals(TRANSFORM_COLS_LOWER)) {
      for (int i = 0; i < columnCount; i++) {
        columns[i] = columns[i].toLowerCase();
      }
    } else if (transformColumnLabels.equals(TRANSFORM_COLS_UPPER)) {
      for (int i = 0; i < columnCount; i++) {
        columns[i] = columns[i].toUpperCase();
      }
    }
  }

  // fills the buffer
  // sets the buffer to null, if there is no more line to read
  private void readBuffer() throws IOException {
    // reset
    curBufferSize = 0;
    cursor = 0;

    if (inReader == null) {
      buffer = null;

      return;
    }

    if (buffer == null) {
      buffer = new String[bufferCapacity];
    }

    String line;
    int i = 0;

    for (; i < bufferCapacity;) {
      line = inReader.readLine();

      if (line == null) {
        // alle lines read, no more left.
        closeInputStream();

        break;
      } else {
        if (commentIndicator != null) {
          // check if line is a comment line,
          // rather than a line of data.
          if (line.trim().startsWith(commentIndicator)) {
            continue;
          }
        }

        buffer[i++] = line;
      }
    }

    curBufferSize = i;
  }

  // Tokenizes the given line and stores the tokens in a String array.
  // Based on the value of the store-param the method behaves in two
  // ways.
  // if store has a non-null value, the tokens are stored in the passed
  // in array. if the lines has less tokens than store.length the positions
  // after the last token are filled with empty strings.
  // if the line has more tokens than store.length, all redundant tokens
  // are discarded.
  // 
  // If the store-param is null an new array will be created and returned.
  // The new array will have the same length as the line has tokens.
  private void tokenizeLine(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line, cellDelimiter, true);

    lineTokens.clear();

    //        if (store == null) {
    //            StringTokenizer tok = new StringTokenizer(line, cellDelimiter,
    // false);
    //            store = new String[tok.countTokens()];
    //        }
    // read the values and store them in the
    // lineTokens-ArrayList.
    boolean lastWasDelimiter = true;
    String tok = null;

    //        int i = 0;
    //        int co = store.length;
    //        while (i < co) {
    while (tokenizer.hasMoreTokens()) {
      tok = tokenizer.nextToken();

      if (tok.equals(cellDelimiter)) {
        if (lastWasDelimiter) {
          tok = "";
        } else {
          lastWasDelimiter = true;

          continue;
        }
      } else {
        lastWasDelimiter = false;
      }

      if (shallTrimValues) {
        lineTokens.add(tok.trim());
      } else {
        lineTokens.add(tok);
      }
    }

    //            else {
    //                tok = "";
    //            }
    //
    //            store[i] = tok;
    //            i++;
    //        }
    // Ende while
    //        return store;
  }

  private void closeInputStream() throws IOException {
    log.debug("Closing output stream");

    try {
      if (inReader != null) {
        inReader.close();
      }
    } finally {
      inReader = null;
    }
  }

  /**
   * @return Returns the encoding.
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * @param encoding
   *            The encoding to set.
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }
}
