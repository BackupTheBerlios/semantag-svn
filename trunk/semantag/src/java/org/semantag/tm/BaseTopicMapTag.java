// $Id: BaseTopicMapTag.java,v 1.3 2004/12/29 21:30:26 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapProvider;
import org.tm4j.topicmap.TopicMapProviderFactory;
import org.tm4j.topicmap.utils.IDGenerator;
import org.tm4j.topicmap.utils.IDGeneratorFactory;

/**
 * Base class for tags that operate on topicmaps.
 *
 * @author Niko Schmuck
 * @author cf
 */
public abstract class BaseTopicMapTag extends BaseTMTag implements ContextTopicMap{
  /** The Log to which logging calls will be made. */
  protected static final Log log = LogFactory.getLog(BaseTopicMapTag.class);
  protected TopicMapProvider tm_provider;

  //***********************************************
  // the baselocator for the provided topicmap
  private String baselocator;

  // the topicmap that this tag provides
  private TopicMap topicmap;
  
  
  /**
   * @return the topicmap that this tag provides.
   */
    public TopicMap getTopicMap() {
        return topicmap;
    }
    
  /**
   * Stores the topicmap so that it can 
   * be retrieved in one of three different manners:
   * - call getTopicMap()
   * - get the value of the variable <code>var</code>
   * from context
   * - call getDefaultTopicMap()
   * @param tm
   */
  protected void storeTopicMap(TopicMap tm) {
      // store it locally, so that a call to
      // getTopicMap() returns it
      topicmap = tm;
      
      // store it as the default topicmap
      // if no default topicmap is set yet
      if(context.getVariable(Dictionary.KEY_TOPICMAP) == null){
          context.setVariable(Dictionary.KEY_TOPICMAP, tm);
      }
      
      // bind it to a variable, if a variable was specified
      storeObject(tm);
      
      // Put also ID generator to jelly context
      IDGenerator idGenerator = IDGeneratorFactory.newIDGenerator();

      context.setVariable(Dictionary.KEY_IDGENERATOR, idGenerator);
    }
  
  /**
   * Initialises a new TopicMapProvider from the default
   * TopicMapProviderFactory
   *
   */
  protected void initialise() {
    initialise(org.tm4j.topicmap.memory.TopicMapProviderFactoryImpl.class);
  }

  /**
   * Initialises a TopicMapProvider with the help of the given 
   * Factory class
   * @param providerFactoryClass
   */
  protected void initialise(Class providerFactoryClass) {
    if (log.isDebugEnabled()) {
      log.debug("Initialise TopicMapProvider from factory class: " +
                providerFactoryClass);
    }

    try {
      TopicMapProviderFactory tmpf = (TopicMapProviderFactory) providerFactoryClass.newInstance();

      //System.getProperties().put(TopicMapProvider.OPT_STATIC_MERGE, Boolean.TRUE);
      
      tm_provider = tmpf.newTopicMapProvider(System.getProperties());
      
    } catch (Exception e) {
      throw new RuntimeException("Could not initialise topic map provider: " +
                                 e.toString());
    }
  }

  /**
   * Helper that creates a locator for the given address.
   * The Locator is created with the "URI" notation scheme.
   * @return
   * @throws JellyTagException
   */
  protected Locator createLocator(String adress) throws JellyTagException {
   return super.createLocator(adress, tm_provider.getLocatorFactory(),log);
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
   * Sets the adress of the baselocator for the topicmap
   * @param baselocator
   */
  public void setBaselocator(String baselocator) {
    this.baselocator = baselocator;
  }


  /**
   * Overrides the method from the superclass and blocks
   * the setting of a source locator since topicmaps are not
   * referenced by source locators.
   * 
   * @jelly
   *    ignore="true"
   */
    public void setSourceLocator(String sourceLocator) {
    }

    /**
     * Overrides the method from the superclass and blocks
     * the setting of an id since topicmaps are not
     * referenced by ids.
     * 
     * @jelly
     *    ignore="true"
     */

    public void setId(String id) {
    }
}
