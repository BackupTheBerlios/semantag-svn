// $Id: TologTag.java,v 1.2 2004/12/09 16:37:31 c_froehlich Exp $
package org.semantag.tm;

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.tm4j.tologx.QueryEvaluator;
import org.tm4j.tologx.QueryEvaluatorFactory;
import org.tm4j.tologx.TologParserException;
import org.tm4j.tologx.TologProcessingException;
import org.tm4j.tologx.TologResultsSet;

import org.tm4j.topicmap.TopicMap;

/**
 * Executes its body as a tolog query and stores the result
 * in the variable named by the <code>var</code>attribute.
 * 
 * 
 * @jelly
 *  name="tolog"
 * 
 * @author cf
 */
public class TologTag extends BaseTMTag {
  /** The Log to which logging calls will be made. */
  protected static final Log log = LogFactory.getLog(TologTag.class);
  private String var = null;
  private String query = null;

  protected void validate() throws JellyTagException {
    if (!isSpecified(getVar())) {
      throw new MissingAttributeException("var");
    }

    if (!isSpecified(query)) {
      throw new JellyTagException("The body of the tolog-tag must contain the query.");
    }
  }

  public void doTag(XMLOutput output)
             throws MissingAttributeException, JellyTagException {
    query = getBodyText();
    validate();

    doQuery();
  }

  protected void doQuery() throws JellyTagException {
    TopicMap tm = getTopicMapFromContext(null);
    
    if(log.isDebugEnabled()){
        log.debug("Querying topicmap " + tm.getBaseLocator().getAddress() + ": \n" +
            query);
    }
    
    QueryEvaluator qE = QueryEvaluatorFactory.newQueryEvaluator(tm);

    if (qE == null) {
      String s = "Unable to get QueryEvaluator for TopicMap " + tm;

      throw new JellyTagException(s);
    }

    // evaluate the query
    TologResultsSet results = null;

    try {
      results = qE.execute(query);
      if(log.isDebugEnabled())
          log.info("result set returned with " + results.getNumRows() + " rows.");
      context.setVariable(getVar(), new TologResultSetWrapper(results));
    } catch (TologParserException e1) {
      String s = "Caught " + e1.getClass() + " - " + e1.getStackTrace();

      s += (" while evaluating query " + query);
      log.error(s, e1);
      throw new UnsupportedOperationException(s);
    } catch (TologProcessingException e1) {
      String s = "Caught " + e1.getClass() + " - " + e1.getStackTrace();

      s += (" while evaluating query " + query);
      log.error(s, e1);
      throw new UnsupportedOperationException(s);
    }
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
}
