// $Id: TopicMapBaseTag.java,v 1.1 2004/08/24 00:12:28 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.net.Locator;
import org.tm4j.net.LocatorFactoryException;

import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderFactory;

/**
 * Base class for tags that operate on topicmaps.
 *
 * @author Niko Schmuck
 * @author cf
 */
public abstract class TopicMapBaseTag extends BaseTag {
  /** The Log to which logging calls will be made. */
  protected static final Log log = LogFactory.getLog(TopicMapBaseTag.class);
  protected TopicMapProvider tm_provider;

  //***********************************************
  // Properties
  private String baselocator;

  // file to read the tm from
  private String file;

  /**
   * Initialises a new TopicMapProvider from the default
   * TopicMapProviderFactory
   *
   */
  protected void initialise() {
    initialise(org.tm4j.topicmap.memory.TopicMapProviderFactoryImpl.class);
  }

  protected void initialise(Class providerFactoryClass) {
    if (log.isDebugEnabled()) {
      log.debug("Initialise TopicMapProvider from factory class: " +
                providerFactoryClass);
    }

    try {
      TopicMapProviderFactory tmpf = (TopicMapProviderFactory) providerFactoryClass.newInstance();

      tm_provider = tmpf.newTopicMapProvider(System.getProperties());
    } catch (Exception e) {
      throw new RuntimeException("Could not initialise topic map provider: " +
                                 e.toString());
    }
  }

  protected Locator createBaseLocator() throws JellyTagException {
    if (log.isDebugEnabled()) {
      log.debug("Create baseLocator for String: " + baselocator);
    }

    try {
      return tm_provider.getLocatorFactory().createLocator("URI", baselocator);
    } catch (LocatorFactoryException e) {
      throw new JellyTagException(e);
    }
  }

  // ****************************************************************************
  // Setter / Getter

  /**
   * @return Returns the baselocator.
   */
  public String getBaselocator() {
    return baselocator;
  }

  /**
   * @param baselocator
   *            The baselocator to set.
   */
  public void setBaselocator(String baselocator) {
    this.baselocator = baselocator;
  }

  /**
   * @return Returns the file.
   */
  public String getFile() {
    return file;
  }

  /**
   * @param file
   *            The file to set.
   */
  public void setFile(String file) {
    this.file = file;
  }
}
