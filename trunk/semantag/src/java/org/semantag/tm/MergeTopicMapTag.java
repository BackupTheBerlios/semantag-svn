// $Id: MergeTopicMapTag.java,v 1.2 2004/12/09 16:37:31 c_froehlich Exp $
package org.semantag.tm;

import java.io.File;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.TopicMap;

/**
 * Class providing a merge-in tag to the existing topic map in context.
 * Requires that a topic map has been initialised already. This tag does not
 * support child elements.
 *
 * @jelly
 *  name="mergeTopicMap"
 * 
 * @author Niko Schmuck
 */
public class MergeTopicMapTag extends BaseTMTag {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(MergeTopicMapTag.class);
  
  // the filename of the topicmap that will be merged 
  // in the current context topic map
  private String filename;

  // The name of a variable that holds the
  // topicmap into which the other topicmap 
  // shall be merged in
  private String tmVar;
  
  

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {

      TopicMap tm = getTopicMapFromContext(tmVar);
      if(tm == null) throw new JellyTagException("There is no topicmap in context to merge with");
      
      File f = new File(filename);

      log.debug("Merging in topic map from file " + f.getAbsolutePath());
    
      tmEngine.mergeTopicMap(tm, f);
  }

 
  public String getTmVar() {
      return tmVar;
  }
  public void setTmVar(String tmVar) {
      this.tmVar = tmVar;
  }
    public void setFilename(String filename) {
      this.filename = filename;
    }

}
