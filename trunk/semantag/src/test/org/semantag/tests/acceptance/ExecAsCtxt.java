// $Id: ExecAsCtxt.java,v 1.1 2004/10/26 19:50:31 niko_schmuck Exp $

package org.semantag.tests.acceptance;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.XMLOutput;
import org.xml.sax.InputSource;


/**
 *
 * @author Niko Schmuck
 */
public class ExecAsCtxt {

  public static void main(String[] args) throws JellyException, IOException {

    OutputStream output = new ByteArrayOutputStream();
    JellyContext context = new JellyContext();

    context.setVariable("demovar", "hallo");

    XMLOutput xmlOutput = XMLOutput.createXMLOutput(output);
    
    // not copied to CLASSPATH currently
    InputSource is = new InputSource(ExecAsCtxt.class.getResourceAsStream("/net/sf/semantag/resource/demo.script"));
    
    // InputSource is = new InputSource(ExecAsCtxt.class.getResourceAsStream("/net/sf/semantag/resource/demo.script"));
    context.runScript("src/test/net/sf/semantag/resource/demo.script", xmlOutput);
    
    xmlOutput.flush();
    
    System.out.println(" demovar: " + context.getVariable("demovar"));
    
    System.out.println("result: " + xmlOutput.toString());
  }
}
