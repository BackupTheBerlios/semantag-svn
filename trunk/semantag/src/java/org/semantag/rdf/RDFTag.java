package org.semantag.rdf;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;

/**
 * Makes a new jena RDF model and fills it from the given url
 * 
 * @jelly
 *  name="rdf"
 * 
 * @jelly.nested 
 *  name="object" 
 *  desc="retrieves a list of objects contained in this model" 
 *  required="no"
 *
 * @author cf
 * @version 0.1, created on 11.08.2004
 */
public class RDFTag extends TagSupport {
  private String modelroot = System.getProperty("java.io.tmpdir");
  private String modelname = "defaultModel";
  private String url = null;
  private Model model = null;

  private void validate() throws MissingAttributeException {
    if (url == null) {
      throw new MissingAttributeException("Missing required attribute: url");
    }
  }

  /* (non-Javadoc)
   * @see org.apache.commons.jelly.Tag#doTag(org.apache.commons.jelly.XMLOutput)
   */
  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    validate();
    model = makeAndFillModel();
    invokeBody(output);
  }

  protected Model makeAndFillModel() {
    ModelMaker mm = ModelFactory.createFileModelMaker(modelroot);
    Model model = mm.createModel(modelname);

    model.read(url);

    return model;
  }

  public Model getModel() {
    return model;
  }

  // SETTER / GETTER

  /**
   * @return Returns the modelname.
   */
  public String getModelname() {
    return modelname;
  }

  /**
   * Sets the name of the model.
   * @jelly
   *    required="no"
   *    default="defaultModel"
   * 
   * @param modelname The modelname to set.
   */
  public void setModelname(String modelname) {
    this.modelname = modelname;
  }

  /**
   * @return Returns the modelroot.
   */
  public String getModelroot() {
    return modelroot;
  }

  /**
   * Sets the path where the model will be stored.
   * 
   * @jelly
   *    required="no"
   *    default="value of the system property \"java.io.tmpdir\""
   * @param modelroot The modelroot to set.
   */
  public void setModelroot(String modelroot) {
    this.modelroot = modelroot;
  }

  /**
   * @return Returns the url.
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the url from which the rdf model will be filled.
   * @jelly
   *    required="yes"
   * 
   * @param url The url to set.
   */
  public void setUrl(String url) {
    this.url = url;
  }
}
