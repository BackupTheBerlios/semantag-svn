// $Id: BaseTagWithId.java,v 1.1 2004/10/26 19:49:48 niko_schmuck Exp $
package org.semantag.tm;

import org.tm4j.topicmap.utils.IDGenerator;

/**
 * Base class for jelly topic map tags, which provide ID access.
 *
 * @author Niko Schmuck
 */
public abstract class BaseTagWithId extends BaseTag {
  private String id;
  private boolean generatedID = false;

  /**
   * The ID will be normalized to avoid later conflicts for XML processing.
   */
  public void setId(String id) {
    this.id = normalize(id);
  }

  /**
   * Returns the ID as string object, if no ID was set an automatically
   * generated ID (see {@link IDGenerator}) will be returned.
   */
  public String getId() {
    if (id == null) {
      IDGenerator gen = (IDGenerator) context.getVariable(KEY_IDGENERATOR);

      id = gen.getID();
      generatedID = true;
    }

    return id;
  }

  /**
   * @return Returns If the ID was automatically generated true,
   *         otherwise false.
   */
  public boolean isGeneratedID() {
    if (id == null) {
      generatedID = true;
    }

    return generatedID;
  }
}
