package org.semantag.tagdoc;
import java.lang.reflect.Modifier;

/**
 * 
 * @author cf
 * @version 0.1, created on 05.12.2004
 */
public class TagdocUtil {

    private static final String JELLY_TAG_CLASSNAME = "org.apache.commons.jelly.Tag";
    final static Class jellyTagClass;

    static{
        try {
            jellyTagClass = Class.forName(JELLY_TAG_CLASSNAME);
        } catch (ClassNotFoundException e) {
            // jelly is not accessible. Bas
            e.printStackTrace();
            throw new RuntimeException("Unable to access class "+JELLY_TAG_CLASSNAME);
        }
        
    }
    /**
     * Checks whether a class is considered as
     * being a JellyTag.
     * 
     * This method performs the following checks:
     * <ul>
     * <li>Does the given class implements the interface
     * {@link org.apache.commons.jelly.Tag}</li>
     * <li>is the class not an abstract class</li>
     * </ul>
     * 
     * @param classname
     * @return true if the Class with given name is considered to be a JellyTag.
     * @throws ClassNotFoundException
     *             if no class could be found for the given name
     */
    public static boolean isJellyTag(String classname)
            throws ClassNotFoundException

    {
        Class probe = Class.forName(classname);
        if (Modifier.isAbstract(probe.getModifiers()))
            return false; // abstract classes are not considered as Tags

        return jellyTagClass.isAssignableFrom(probe);
    }
}