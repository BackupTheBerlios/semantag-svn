package org.semantag.tagdoc;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.selectors.BaseExtendSelector;

/**
 * A Selector that selects only classes of type JellyTag.
 * 
 * @author cf
 */

public class IsJellyTagSelector extends BaseExtendSelector {

    public boolean isSelected(File arg0, String classpart, File arg2)
            throws BuildException {
        
        if(classpart.endsWith(".java"))
                classpart = classpart.substring(0, classpart.length()-5);
        
        classpart = classpart.replace('/','.');
    
        
        
        try {
            return TagdocUtil.isJellyTag(classpart);
                
        } catch (Throwable e) {
            // catching Throwable since a
            // NoClassDefFoundError may be thrown
            // by TagUtil is as well
            e.printStackTrace();
            throw new BuildException(
                    "While searching for a class with name " + classpart, e);
        }
    }

    
    
}