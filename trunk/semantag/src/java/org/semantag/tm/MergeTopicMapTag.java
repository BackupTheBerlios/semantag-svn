// $Id: MergeTopicMapTag.java,v 1.3 2004/12/09 21:19:58 c_froehlich Exp $
package org.semantag.tm;

import java.io.File;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.TopicMap;

/**
 * Merges a topicmap from the filesystem with the current topic map
 * in context
 * 
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

  // The topicmap into which the other topicmap 
  // shall be merged in
  private TopicMap topicmap;
  
  

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {

      if(topicmap == null){
          topicmap = getTopicMapFromContext(null);
          if(topicmap == null) throw new JellyTagException("There is no topicmap in context to merge with");
      }
      File f = new File(filename);

      log.debug("Merging in topic map from file " + f.getAbsolutePath());
    
      tmEngine.mergeTopicMap(topicmap, f);
  }

 

  /**
   * Sets the topicmap to which this tag refers to.
   * 
   * @jelly
   *  required="no"
   *  default="The topicmap currently in context"
   */
  public void setTopicmap(TopicMap tm) {
      topicmap = tm;
  }
  
  /**
   * Sets the file for the topicmap to merge. If the file has the extensions "ltm" or "txt"
   * than it is assumed that the topicmap is available in ltm-notation. 
   * Otherwise it is assumed that the file uses xtm-notation.
   * 
   * @jelly
   *    required="yes"
   *  
   * 
   * @param file
   */

    public void setFilename(String filename) {
      this.filename = filename;
    }

}
