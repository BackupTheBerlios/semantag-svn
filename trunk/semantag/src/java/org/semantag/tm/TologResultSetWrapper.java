package org.semantag.tm;

import java.util.List;

import org.tm4j.tologx.TologResultsSet;

/**
 * Helper that copies a tolog result set into an
 * array of objects
 * @author cf
 * @version 0.1, created on 13.08.2004
 */
public class TologResultSetWrapper {
  private Object[][] data;

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
    data = new Object[rows][cols];

    for (int r = 0; r < rows; r++) {
      List rowData = rs.getRow(r);

      for (int c = 0; c < cols; c++) {
        Object value = rowData.get(c);
        data[r][c] = value;
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
