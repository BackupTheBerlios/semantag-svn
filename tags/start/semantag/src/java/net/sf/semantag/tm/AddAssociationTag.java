// $Id: AddAssociationTag.java,v 1.1 2004/08/24 00:12:29 niko_schmuck Exp $
package net.sf.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.DuplicateObjectIDException;
import org.tm4j.topicmap.TopicMap;

import java.beans.PropertyVetoException;

import java.util.Iterator;

/**
 * Creates an association within the context's topic map under
 * the specified ID. If no ID was explicitly given the ID
 * will be auto-generated.
 *
 * @author Niko Schmuck
 */
public class AddAssociationTag extends BaseTagWithId {
  /** The Log to which logging calls will be made. */
  private static final Log log = LogFactory.getLog(AddAssociationTag.class);
  private Association association;

  public Association getAssociation() {
    return association;
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    try {
      TopicMap tm = getTopicMap();

      // verify that association does not already exist, if ID was specified
      boolean found = false;
      String assocId = getId();

      if (!isGeneratedID()) {
        // TODO: investigate, if there is a more performant way than loop over every association in TM
        Iterator it = tm.getAssociationsIterator();

        while (it.hasNext()) {
          Association curAssoc = (Association) it.next();

          if (curAssoc.getID().equals(assocId)) {
            found = true;
            log.info("Association with id '" + assocId + "' already exists.");

            break;
          }
        }
      }

      // now check if association should be created
      if (!found) {
        association = tm.createAssociation(assocId);
        getBody().run(context, output);
      }
    } catch (DuplicateObjectIDException e) {
      throw new JellyTagException(e);
    } catch (PropertyVetoException e) {
      throw new JellyTagException(e);
    }
  }
}
