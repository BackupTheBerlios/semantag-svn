package org.semantag.tm;

import org.tm4j.net.Locator;

import org.tm4j.tologx.TologResultsSet;

import org.tm4j.topicmap.TopicMapObject;

import java.util.List;

/**
 *
 * @author cf
 * @version 0.1, created on 13.08.2004
 */
public class TologResultSetWrapper {
  private String[][] data;

  public TologResultSetWrapper(TologResultsSet rs) {
    if (rs == null) {
      return;
    }

    int rows = rs.getNumRows();
    int cols = rs.getNumCols();

    if ((rows < 1) || (cols < 1)) {
      return;
    }

    // copy data
    data = new String[rows][cols];

    for (int r = 0; r < rows; r++) {
      List rowData = rs.getRow(r);

      for (int c = 0; c < cols; c++) {
        Object value = rowData.get(c);

        if (value == null) {
          data[r][c] = null;
        } else if (value instanceof Locator) {
          data[r][c] = ((Locator) value).getAddress();
        } else if (value instanceof TopicMapObject) {
          data[r][c] = ((TopicMapObject) value).getID();
        } else {
          data[r][c] = value.toString();
        }
      }
    }
  }

  public Object[][] getRows() {
    if (data == null) {
      return new Object[0][0];
    } else {
      return data;
    }
  }
  
  public int getRowCount(){
      if(data == null) return 0;
      else return data.length;
  }
  
  public boolean hasRows(){
      return getRowCount() > 0;
  }
}
