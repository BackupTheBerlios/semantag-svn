// $Id: AddAssociationTag.java,v 1.3 2004/09/12 18:22:19 c_froehlich Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.TopicMap;

/**
 * Creates an association within the context's topic map under
 * the specified ID. If no ID was explicitly given the ID
 * will be auto-generated.
 *
 * @author Niko Schmuck
 * @author cf
 */
public class AddAssociationTag extends BaseTMTag implements ContextAssociation, ReferenceTopicMap{

  // The Log to which logging calls will be made. 
  private static final Log log = LogFactory.getLog(AddAssociationTag.class);

  // The association that was added by a call to doTag(...)
  private Association association;

  // The name of a variable that is bound to a topicmap
  private String tmVar = null;

  /**
   * @return the association, that was created by this tag.
   */
  public Association getAssociation() {
    return association;
  }

  /**
   * Creates the association, stores it in the context 
   * and passes control to the body of the tag.
   */
  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {

      if(log.isDebugEnabled())
          log.debug("Adding association (ID "+getId()+" / SL: "+getSourceLocator()+")");
      
      // get map from context
      TopicMap tm = getTopicMapFromContext(tmVar);
      
      // create association
      association = CreatorUtil.createAssociation(tm, getId(), getSourceLocator());
      
      // set variable
      storeObject(association);
      
      // process body
      getBody().run(context, output);

  }

  
  /**
   * @return the name of the variable that is used
   * to lookup the topicmap, to which the new tag will be 
   * added
   */
  public String getTmVar() {
      return tmVar;
  }

  /**
   * sets the name of the variable that holds the topicmap, 
   * to which the new tag will be added
   */
  public void setTmVar(String tmVar) {
      this.tmVar = tmVar;
  }
}   
  
