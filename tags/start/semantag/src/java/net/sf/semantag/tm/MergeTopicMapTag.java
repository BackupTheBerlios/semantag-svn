// $Id: MergeTopicMapTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderException;
import org.tm4j.topicmap.source.SerializedTopicMapSource;
import org.tm4j.topicmap.source.TopicMapSource;

import java.io.File;
import java.io.FileNotFoundException;

import java.net.MalformedURLException;

/**
 * Class providing a merge-in tag to the existing topic map in context.
 * Requires that a topic map has been initialised already. This tag does not
 * support child elements.
 *
 * @author Niko Schmuck
 */
public class MergeTopicMapTag extends BaseTag {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(MergeTopicMapTag.class);
  private String filename;

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    TopicMap tm = getTopicMap();
    File f = new File(filename);

    log.info("Merging in topic map from file " + f.getAbsolutePath());
    mergeTopicMap(tm, f);
  }

  public void mergeTopicMap(TopicMap basetm, File file) {
    TopicMapProvider provider = basetm.getProvider();

    try {
      TopicMapSource source = new SerializedTopicMapSource(file);
      TopicMap map = provider.addTopicMap(source, basetm); // TODO
    } catch (TopicMapProviderException e) {
      throw new RuntimeException("Could not merge topic map from '" +
                                 file.getAbsolutePath() + "': " + e);
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Could not locate file to merge: " +
                                 file.getAbsolutePath());
    } catch (MalformedURLException e) {
      throw new RuntimeException("Could not create URL from file: " + e);
    }
  }
}
