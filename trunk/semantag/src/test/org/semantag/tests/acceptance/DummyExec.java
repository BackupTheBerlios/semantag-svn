// $Id: DummyExec.java,v 1.1 2004/10/26 19:50:30 niko_schmuck Exp $

package org.semantag.tests.acceptance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.jelly.impl.Embedded;


/**
 *
 * @author Niko Schmuck
 */
public class DummyExec {

  public static void main(String[] args) throws IOException {
    
    String scriptAsString = "<j:jelly xmlns:j=\"jelly:core\" xmlns:sql=\"jelly:sql\">myvar ist ${myvar}.</j:jelly>";
    
    Embedded embedded = new Embedded();
    OutputStream out = new ByteArrayOutputStream();
    embedded.setOutputStream(out);
    embedded.setVariable("myvar","some-object");

    embedded.setScript(scriptAsString);
    out.flush();

    boolean statusOK = embedded.execute();
    if(!statusOK)
    {
      String errorMsg = embedded.getErrorMsg();
    }
    System.out.println("result: " + out.toString() + ".");
    
  }
}
