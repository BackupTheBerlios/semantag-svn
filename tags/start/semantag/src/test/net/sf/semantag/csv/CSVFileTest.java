package net.sf.semantag.csv;

import junit.framework.TestCase;

import net.sf.semantag.TestData;
import net.sf.semantag.csv.CSVFile;

import java.io.IOException;

import java.util.List;

/**
 *
 * @author cf
 * @version 0.1, created on 02.08.2004
 */
public class CSVFileTest extends TestCase {
  private CSVFile csvfile = null;

  public void testSetBufferCapacity() {
  }

  public void testSetTransformColumnLabels() {
  }

  // test that headers can be read and that 
  // they are not transformed per default
  public void testGetColumnHeaders() throws Exception {
    csvfile.open();

    String[] headers = csvfile.getColumnLabels();

    assertEquals(2, headers.length);
    assertEquals("ID", headers[0]);
    assertEquals("Name", headers[1]);
  }

  // test transformation of headers 
  // to lower and to upper case.
  public void testTransformColumnHeaders() throws Exception {
    csvfile.setTransformColumnLabels(CSVFile.TRANSFORM_COLS_UPPER);
    csvfile.open();

    String[] headers = csvfile.getColumnLabels();

    assertEquals("ID", headers[0]);
    assertEquals("NAME", headers[1]);
    csvfile.close();

    csvfile = defaultTestFile();
    csvfile.setTransformColumnLabels(CSVFile.TRANSFORM_COLS_LOWER);
    csvfile.open();
    headers = csvfile.getColumnLabels();
    assertEquals("id", headers[0]);
    assertEquals("name", headers[1]);
    csvfile.close();
  }

  // tests the hasNext() / next()-Iterating
  public void testReadRows() throws Exception {
    csvfile.open();

    // read first row
    assertTrue(csvfile.hasNext());

    List row = csvfile.getNext();

    assertEquals(2, row.size());
    assertEquals("12-345", row.get(0));
    assertEquals("Brown", row.get(1));

    // read second row
    assertTrue(csvfile.hasNext());
    row = csvfile.getNext();
    assertEquals(2, row.size());
    assertEquals("67-890", row.get(0));
    assertEquals("Blue", row.get(1));

    assertFalse(csvfile.hasNext());
  }

  public void testDefaultCellDelimiter() throws IOException {
    // assert that tabulator is the default
    assertEquals("\t", csvfile.getCellDelimiter());
  }

  // tests getter/setter
  public void testGetSetCellDelimiter() throws IOException {
    // assert that setting the delimiter
    // triggers some change
    csvfile.setCellDelimiter(";");
    assertEquals(";", csvfile.getCellDelimiter());
  }

  public void testSetUnusedCellDelimiter() throws IOException {
    csvfile.setCellDelimiter(";");

    // since an unused celldelimiter is passed in
    // the file should treat each row as one cell
    csvfile.open();

    String[] headers = csvfile.getColumnLabels();

    assertEquals(1, headers.length);
    assertEquals("ID\tName", headers[0]);

    // read first row
    assertTrue(csvfile.hasNext());

    List row = csvfile.getNext();

    assertEquals(1, row.size());
    assertEquals("12-345\tBrown", row.get(0));

    // read second row
    assertTrue(csvfile.hasNext());
    row = csvfile.getNext();
    assertEquals(1, row.size());
    assertEquals("67-890\tBlue", row.get(0));

    assertFalse(csvfile.hasNext());
  }

  /* read a file whoose specs differ in some
   * points from the defaults.
   * - it contains lines with comments
   * - it does not contain headers
   * - rows have different count of items
   * - it uses a custom cell delimiter
   */
  public void testAdvancedFile() throws Exception {
    csvfile = advancedTestFile();
    csvfile.open();

    assertTrue(csvfile.hasNext());

    List row = csvfile.getNext();

    assertEquals(3, row.size());
    assertEquals("13350", row.get(0));
    assertEquals(" DE", row.get(1));
    assertEquals(" BW", row.get(2));

    assertTrue(csvfile.hasNext());
    row = csvfile.getNext();
    assertEquals(4, row.size());
    assertEquals("13351", row.get(0));
    assertEquals(" DE", row.get(1));
    assertEquals(" RP", row.get(2));
    assertEquals(" Jaguar", row.get(3));

    assertFalse(csvfile.hasNext());
  }

  /**
   * Tests the option, that
   * cell values will be trimmed by
   * the csv file
   * @throws Exception
   */
  public void testTrimValues() throws Exception {
    csvfile = advancedTestFile();
    csvfile.setShallTrimValues(true);
    csvfile.open();

    assertTrue(csvfile.hasNext());

    List row = csvfile.getNext();

    assertEquals(3, row.size());
    assertEquals("13350", row.get(0));
    assertEquals("DE", row.get(1));
    assertEquals("BW", row.get(2));

    assertTrue(csvfile.hasNext());
    row = csvfile.getNext();
    assertEquals(4, row.size());
    assertEquals("13351", row.get(0));
    assertEquals("DE", row.get(1));
    assertEquals("RP", row.get(2));
    assertEquals("Jaguar", row.get(3));

    assertFalse(csvfile.hasNext());
  }

  /* (non-Javadoc)
   * @see junit.framework.TestCase#setUp()
   */
  protected void setUp() throws Exception {
    super.setUp();
    csvfile = defaultTestFile();
  }

  protected void tearDown() throws Exception {
    if (csvfile != null) {
      csvfile.close();
    }
  }

  private CSVFile defaultTestFile() throws Exception {
    return new CSVFile(TestData.getFileFromResource(TestData.CSVTEST));
  }

  /*
   * @return a csvfile whose settings differ
   * from the defaults in some points
   * @throws Exception
   */
  private CSVFile advancedTestFile() throws Exception {
    CSVFile f = new CSVFile(TestData.getFileFromResource(TestData.ADVANCEDCSVTEST));

    f.setCellDelimiter(";");
    f.setCommentIndicator("#");
    f.setFirstLineContainsHeaders(false);

    return f;
  }
}
