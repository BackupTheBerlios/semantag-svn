package org.semantag.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;

/**
 *
 * @author cf
 * @version 0.1, created on 01.08.2004
 */
public class ObjectTag extends TagSupport {
  /** The Log to which logging calls will be made. */

  //private static final Log log = LogFactory.getLog(TagSupport.class);

  /**
   * Name of the variable to hold the value
   * Required
   */
  private String var;

  /**
   * property to filter the list of objects
   *
   */
  private String property;
  private String propertyNS;

  /**
   * Whether the execution of nested tags should be
   * limited to the first hit
   *
   */
  private boolean limitToFirst = true;

  /**
   *
   */
  public ObjectTag() {
    super();
  }

  protected void validate() throws MissingAttributeException {
    if (var == null) {
      throw new MissingAttributeException("Attribute 'var' must be specified");
    }

    if (((property != null) && (propertyNS == null)) ||
        ((property == null) && (propertyNS != null))) {
      throw new MissingAttributeException("Either both or none of attributes 'property' and 'propertyNS' must be specified.");
    }
  }

  /* (non-Javadoc)
   * @see org.apache.commons.jelly.Tag#doTag(org.apache.commons.jelly.XMLOutput)
   */
  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    validate();

    // get the parent
    Model model = getModel();

    NodeIterator it;

    if (property != null) {
      // limit objects to those with a given property
      Property p = model.getProperty(propertyNS, property);

      it = model.listObjectsOfProperty(p);
    } else {
      it = model.listObjects();
    }

    while (it.hasNext()) {
      RDFNode n = it.nextNode();

      context.setVariable(var, n.toString());

      if (limitToFirst) {
        break; //stop after first iteration
      }
    }
  }

  /**
   * @return
   */
  private Model getModel() throws JellyTagException {
    RDFTag theTag = (RDFTag) findAncestorWithClass(RDFTag.class);

    if (theTag == null) {
      throw new JellyTagException("Tag rdf:object must be nested in tag rdf:model");
    }

    return theTag.getModel();
  }

  /**
   * @return Returns the var.
   */
  public String getVar() {
    return var;
  }

  /**
   * @param var The var to set.
   */
  public void setVar(String var) {
    this.var = var;
  }

  /**
   * @return Returns the property.
   */
  public String getProperty() {
    return property;
  }

  /**
   * @param property The property to set.
   */
  public void setProperty(String property) {
    this.property = property;
  }

  /**
   * @return Returns the propertyNS.
   */
  public String getPropertyNS() {
    return propertyNS;
  }

  /**
   * @param propertyNS The propertyNS to set.
   */
  public void setPropertyNS(String propertyNS) {
    this.propertyNS = propertyNS;
  }
}
