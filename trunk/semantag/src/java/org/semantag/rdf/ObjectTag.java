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
 * Retrieves a list of objects from the current context model 
 * and calls its body for each member of the list.
 *
 * Optional the list of objects can be restricted to those
 * of a certain property.
 *
 * @jelly
 *  name="object"
 *  
 * @author cf
 * @version 0.1, created on 01.08.2004
 */
public class ObjectTag extends TagSupport {
  /** The Log to which logging calls will be made. */

  //private static final Log log = LogFactory.getLog(TagSupport.class);

  /**
   * Name of the variable that holds the current object
   * in each step of the iteration 
   * @jelly 
   *   required ="yes"
   */
  private String var;


  /**
   * A property to filter the list of objects.
   * This attribute must always be combined with
   * the propertyNS - attribute
   * @jelly 
   *   required ="no"
   */
  private String property;


  /**
   * The namespace of the property to filter the list of objects.
   * This attribute must always be combined with
   * the property - attribute
   * @jelly 
   *   required ="no"
   */
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
      invokeBody(output);
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
   * Name of the variable that holds the current object
   * in each step of the iteration 
   * @param var the name of the variable to set.
   * @jelly 
   *   required ="yes"
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
   * A property to filter the list of objects.
   * This attribute must always be combined with
   * the propertyNS - attribute
   * @param property The property to set.
   * 
   * @jelly 
   *   required ="no, unless propertyNS is set"
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
   * The namespace of the property to filter the list of objects.
   * This attribute must always be combined with
   * the property - attribute
   * @param propertyNS The propertyNS to set.
   * 
   * @jelly 
   *   required ="no, unless property is set"
   */
  public void setPropertyNS(String propertyNS) {
    this.propertyNS = propertyNS;
  }
  
  public boolean isLimitToFirst() {
      return limitToFirst;
  }

  /**
   * Whether the execution of nested tags should be
   * limited to the first member of the list of objects.
     * @jelly 
     *   required ="no"
     *   default="true"
   */
  public void setLimitToFirst(boolean limitToFirst) {
      this.limitToFirst = limitToFirst;
  }

}
