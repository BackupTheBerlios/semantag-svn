/**
 * LICENSE
 * ------------
 * This file is distributed under the provisions of the TM4J license. See
 * http://tm4j.org/LICENSE.TXT for the details of this license.
 */
package org.semantag.tm;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.tm4j.topicmap.*;
import org.tm4j.net.Locator;

/**
 * Collection of static methods to generate names for different TopicMapObjects.
 * TODO: remove MAX_LABEL_LENGTH and pass responsibility for short names to 
 * AMMember.getLabel()
 *
 * @author Niko Schmuck (niko@nava.de)
 * @author Christopn Froehlich (c_froehlich at users.sourceforge.net)
 */
public final class NameUtil {

  public static final int MAX_LABEL_LENGTH = 64;


  public static String labelFor(TopicMapObject tmo) {
    if (tmo instanceof Topic) {
      return labelFor((Topic) tmo);
    } else if (tmo instanceof Member) {
      return labelFor((Member) tmo);
    } else if (tmo instanceof Occurrence) {
      return labelFor((Occurrence) tmo);
    } else if (tmo instanceof TopicMap) {
      return labelFor((TopicMap) tmo);
    } else if (tmo instanceof Association) {
      return labelFor((Association) tmo);
    } else {
      return tmo.getClass().getName() + "[id:" + tmo.getID() + "]";
    }
  }

  
  /**
   * Returns a label for an object.
   * Contains special treatment for objects that 
   * are instances of {@link TopicMapObject}, {@link Collection}
   * or {@link Locator}.
   * For all other objects o.toString() is returned.
   * Treats objects that are instances
   * @param o
   * @return
   */
  public static String labelFor(Object o) {
    if (o instanceof TopicMapObject) {
        return NameUtil.labelFor((TopicMapObject) o);
    } else if (o instanceof Collection && (o != null)) {
        Collection c = (Collection) o;

        if (c.size() == 1) {
            return labelFor(c.iterator().next());
        } else {
            Iterator it = c.iterator();
            String label = "";

            while (it.hasNext()) {
                label += labelFor(it.next());

                if (it.hasNext()) {
                    label += ", ";
                }
            }

            return label;
        }
    } else if (o instanceof Locator){
    	return ((Locator)o).getAddress();
    }
    else {
        return (o == null) ? null : o.toString();
    }
}
 
  /**
   * @return a label for the Topic t.
   * The first try is to generate a label from
   * the basename-Properties.
   * If not succcesfull, next try is to check for classes and
   * use their names, indicating that the actual Topic is
   * an instance of
   * if the method is unable to determine a name for t,
   * the id of t is returned
   */
  public static String labelFor(Topic t) {
      return labelFor(t, true);
  }
  
  
  /**
   * 
   * @param t the topic to get a label for
   * @param includeScope whether the represantation of the
   * baseNames should include eventual themes
   * @return a label for topic t
   */
  public static String labelFor(Topic t, boolean includeScope) {
    if (t == null) {
      return "Topic <null>";
    }

    // the process is controlled by the baseTopic
    Topic tb = t.getBaseTopic();

    if (tb != null && ( !(tb.equals(t)) )){
      //System.out.println(tb+" - "+ t);
        return labelFor(tb);
      }
    else
      return labelForBaseTopic(t, includeScope);
  }

  private static String labelForBaseTopic(Topic t, boolean includeScope) {
    // Trying basename
    String name = basenameOfTopic(t, includeScope);
    if (name != null)
      return name;

    // basename from merged Topics?
    Iterator it = t.getMergedTopics().iterator();
    while (it.hasNext()) {
      name = basenameOfTopic((Topic) it.next(), includeScope);
      if (name != null)
        return name;
    }

    // this topic has no names
    // let's see if it's one of our basic psis
    // TODO re-enable PSI-support
//    name = PSIUtil.getNameForPSI(t);
//    if (name != null)
//      return name;

    // no name.
    // so we use the id?
    return "T [" + t.getID() + "]";
  }

  /**
   * @return the basename for topic t if it could be
   * found, null otherwise
   */
  private static String basenameOfTopic(Topic t, boolean includeScopes) {
    StringBuffer label = null;
    // get all BaseName objects
    Iterator names = t.getNames().iterator();

//    if (!names.hasNext())
//      return null;

    while (names.hasNext()) {
      BaseName name = (BaseName) names.next();
      String n = removeDuplicateWhitespace(name.getData());
      Set scope = name.getScope();
      
      if ( (!includeScopes) || scope == null
        || scope.size() == 0) {
        return n;
      } else {
        // adding all scopedNames
        if (label == null)
          label = new StringBuffer(MAX_LABEL_LENGTH);
        label.append(n).append(" (");
        for (Iterator scopingTopics = scope.iterator(); scopingTopics.hasNext();) {
            Topic theme = (Topic) scopingTopics.next();
            label.append(NameUtil.labelFor(theme, false));
            if(scopingTopics.hasNext()) label.append(", ");
        }
        label.append(") ");
        if (label.length() > MAX_LABEL_LENGTH)
          return label.toString();
      }
    }

    return (label != null) ? label.toString() : null;
  }

  /**
   * @return label for TopicMap
   */
  public static String labelFor(TopicMap map) {
    if (map == null) {
      return "TopicMap <null>";
    }
    String name;
    name = map.getName();
    if ((name != null) && (name.length() > 0)) {
      return name;
    }

    Locator baseLoc = map.getBaseLocator();
    if (baseLoc != null) {
      name = baseLoc.getAddress();

      if ((name != null) && (name.length() > 0)) {
        return name;
      }
    }

    name = map.getID();
    if ((name != null) && (name.length() > 0)) {
      return name;
    }

    return "TopicMap <no name>";
  }

  /**
   * @return label for an Association
   */
  public static String labelFor(Association assoc) {
    StringBuffer title = new StringBuffer(MAX_LABEL_LENGTH);

    // stringify association type
    title.append("A[").append(labelFor(assoc.getType())).append("]");

    return title.toString();
  }

  /**
   * @return label for a Member
   */
  public static String labelFor(Member member) {
    StringBuffer title = new StringBuffer(MAX_LABEL_LENGTH);

    // stringify member type
    title.append("R[").append(labelFor(member.getRoleSpec())).append("]");

    return title.toString();
  }

  /**
   * @return label for an Occurence
   */
  public static String labelFor(Occurrence occ) {
    StringBuffer title = new StringBuffer(MAX_LABEL_LENGTH);

    // stringify member type
    //    if (occ.getType() != null) {
    //      title.append("[").append(labelFor(occ.getType())).append("] ");
    //    }

    // append locator or beginning of
    // ressource data
    String t = occ.getData();

    if (t == null) {
      Locator loc = occ.getDataLocator();
      if (loc != null) {
        t = loc.getAddress();
      }
    }

    if (t != null) {
      String redTitle = removeDuplicateWhitespace(t);
      int free = MAX_LABEL_LENGTH - title.length();
      if (free >= redTitle.length()) {
        title.append(redTitle);
      } else {
        title.append(redTitle.substring(0, free));
      }
    }

    return title.toString();
  }

  /**
	* @return a label for the Locator loc
	*/
  public static String labelFor(Locator loc) {
	 if (loc == null) {
		return "Locator <null>";
	 }

		// the process is controlled by the baseTopic
		String s = loc.getAddress();
		if(s == null) return "";
		s = removeDuplicateWhitespace(s);
		if(s.length() > MAX_LABEL_LENGTH)
			return s.substring(0,MAX_LABEL_LENGTH);
		else 
			return s;
  }

  // ======================================================================

  private static String removeDuplicateWhitespace(String inp) {
    if(inp == null) return null;
    char[] string = inp.toCharArray();
    char[] result = new char[string.length];
    int pos = 0;
    boolean seenWhitespace = false;
    
    // loop over each input character
    for (int i=0; i < string.length; i++) {
      switch (string[i]) {
        case ' ' :
        case '\t' :
        case '\n' :
        case '\r' :
          seenWhitespace = true;
          break;
        default :
          if (seenWhitespace) {
            result[pos] = ' ';
            seenWhitespace = false;
            pos++;
          }
          result[pos] = string[i];
          pos++;
      }
    }

    return new String(result, 0, pos);
  }

}
