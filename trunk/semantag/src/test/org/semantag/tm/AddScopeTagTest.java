package org.semantag.tm;


import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.TagSupport;
import org.semantag.TestData;
import org.semantag.tm.AddScopeTag;
import org.semantag.tm.Dictionary;
import org.semantag.tm.UseAssociationTag;
import org.semantag.tm.UseBasenameTag;
import org.semantag.tm.UseOccurrenceTag;
import org.tm4j.topicmap.Association;
import org.tm4j.topicmap.BaseName;
import org.tm4j.topicmap.Occurrence;
import org.tm4j.topicmap.Topic;
import org.tm4j.topicmap.TopicMap;
import org.tm4j.topicmap.TopicMapObject;

/**
 * 
 * @author cf
 * @version 0.1, created on 06.09.2004
 */
public class AddScopeTagTest extends TMTagTestBase {

    private TopicMap tm;

    private AddScopeTag ast;

    private JellyContext ctx;


    
    // tests to set the scope of an Association
    public void testAddThemeToAssociation() throws Exception {

        Association assoc = (Association)tm.getObjectByID("assoc_without_type");
        Topic theme1 = tm.getTopicByID("theme1"); 
        Topic theme2 = tm.getTopicByID("theme2"); 
        Topic theme3 = tm.getTopicByID("theme3"); 

        // store theme count
        int co = assoc.getScope().size();
        
        // assert preconditions
        assertTrue(!assoc.getScope().contains(theme1));
        assertTrue(!assoc.getScope().contains(theme2));
        
        // specify assoc explicitly
        doAddScope(theme1, assoc, null,assoc);
        
        // ----------------------------------
        // specify assoc implicitly
        // and add second theme to the scope
        UseAssociationTag uat = new UseAssociationTag();
        uat.setAssociation(assoc);
        
        // test
        doAddScope(theme2, null, uat,assoc);

        
        // ----------------------------------
        // assert that explicitly specified associations 
        // have a higher proprity than those specified 
        // implicitly
        Association assoc2 = (Association)tm.getObjectByID("test2-as-01");
        
        // theme count of assoc2 
        int co2 = assoc2.getScope().size();
        
        uat = new UseAssociationTag();
        uat.setAssociation(assoc2);
        
        // test
        doAddScope(theme3, assoc, uat,assoc);
        
        // assoc2 should be unaltered
        assertEquals(co2, assoc2.getScope().size());
        
        // assoc 1 should have three more themes
        assertEquals(co+3, assoc.getScope().size());
        assertTrue(assoc.getScope().contains(theme1));
        assertTrue(assoc.getScope().contains(theme2));
        assertTrue(assoc.getScope().contains(theme3));
        
        
    }
    

    // tests to set the scope of an Occurrence
    public void testAddThemeToOccurrence() throws Exception {

        Occurrence occ = (Occurrence)tm.getObjectByID("occ_without_type");
        Topic theme1 = tm.getTopicByID("theme1"); 
        Topic theme2 = tm.getTopicByID("theme2"); 
        Topic theme3 = tm.getTopicByID("theme3"); 

        // store theme count
        int co = occ.getScope().size();
        
        // assert preconditions
        assertTrue(!occ.getScope().contains(theme1));
        assertTrue(!occ.getScope().contains(theme2));
        
        // specify assoc explicitly
        doAddScope(theme1, occ, null,occ);
        
        // ----------------------------------
        // specify assoc implicitly
        // and add second theme to the scope
        UseOccurrenceTag uat = new UseOccurrenceTag();
        uat.setOccurrence(occ);
        
        // test
        doAddScope(theme2, null, uat,occ);

        
        // ----------------------------------
        // assert that explicitly specified associations 
        // have a higher proprity than those specified 
        // implicitly
        Occurrence occ2 = (Occurrence)tm.getObjectByID("occ_with_type");
        
        // theme count of assoc2 
        int co2 = occ2.getScope().size();
        
        uat = new UseOccurrenceTag();
        uat.setOccurrence(occ2);
        
        // test
        doAddScope(theme3, occ, uat,occ);
        
        // assoc2 should be unaltered
        assertEquals(co2, occ2.getScope().size());
        
        // assoc 1 should have three more themes
        assertEquals(co+3, occ.getScope().size());
        assertTrue(occ.getScope().contains(theme1));
        assertTrue(occ.getScope().contains(theme2));
        assertTrue(occ.getScope().contains(theme3));
        
        
    }

    // tests to set the scope of an Basename
    public void testAddThemeToBasename() throws Exception {

        BaseName bn = (BaseName)tm.getObjectByID("test2-bn1");
        BaseName bn2 = (BaseName)tm.getObjectByID("test2-bn2");
        Topic theme1 = tm.getTopicByID("theme1"); 
        Topic theme2 = tm.getTopicByID("theme2"); 
        Topic theme3 = tm.getTopicByID("theme3"); 

        // store theme count
        int co = bn.getScope().size();
        
        // assert preconditions
        assertTrue(!bn.getScope().contains(theme1));
        assertTrue(!bn.getScope().contains(theme2));
        
        // specify basename explicitly
        doAddScope(theme1, bn, null,bn);
        
        // ----------------------------------
        // specify basename implicitly
        // and add second theme to the scope
        UseBasenameTag uat = new UseBasenameTag();
        uat.setBaseName(bn);
        
        // test
        doAddScope(theme2, null, uat,bn);

        
        // ----------------------------------
        // assert that explicitly specified basenames 
        // have a higher proprity than those specified 
        // implicitly
        
        // theme count of basename2 
        int co2 = bn2.getScope().size();
        
        uat = new UseBasenameTag();
        uat.setBaseName(bn2);
        
        // test
        doAddScope(theme3, bn, uat,bn);
        
        // basename2 should be unaltered
        assertEquals(co2, bn2.getScope().size());
        
        // basename should have three more themes
        assertEquals(co+3, bn.getScope().size());
        assertTrue(bn.getScope().contains(theme1));
        assertTrue(bn.getScope().contains(theme2));
        assertTrue(bn.getScope().contains(theme3));
        
        
    }
    
    
    // tests to assert that the context is resolved
    // step by step from the current tag towards the root tag
    public void testResolveContextLinear() throws Exception {

        Occurrence occ = (Occurrence)tm.getObjectByID("occ_without_type");
        Association assoc = (Association)tm.getObjectByID("assoc_without_type");
        Topic theme1 = tm.getTopicByID("theme1"); 

        // precount
        int coThemesOfAssoc = assoc.getScope().size();
        int coThemesOfOcc = occ.getScope().size();
        

        
        
        // ----------------------------------
        // specify a nested topic/occ -structure implicitly
        UseAssociationTag uat = new UseAssociationTag();
        uat.setAssociation(assoc);
        
        UseOccurrenceTag uot = new UseOccurrenceTag();
        uot.setOccurrence(occ);
        uot.setParent(uat);
        
        // test
        doAddScope(theme1, null, uot,occ);

        // check counts
        // assoc should be unaffected, occ should have one more theme
        assertEquals(coThemesOfAssoc, assoc.getScope().size());
        assertEquals(coThemesOfOcc + 1, occ.getScope().size());
        
    }
    /**
     * Adds a type to either the explicit instance or
     * to the implicit instance.
     * 
     * @param theme
     * @param explInstance
     * @param implInstance
     * @param cmpInstance instance that is expected to be altered,
     * either via explicit or via implicit referencing
     * @throws Exception
     */
    private void doAddScope(Topic theme, TopicMapObject explInstance, 
            TagSupport implInstance, TopicMapObject cmpInstance)
        throws Exception
    {

        
        // set type
        ast.setTheme(theme);

        
        // set the instance both explicit and implicit
        if(explInstance != null) ast.setScopedObject(explInstance);
        if(implInstance != null) ast.setParent(implInstance);

        
        // add a dummy body
        setScriptForTagBody(ast);

        // add theme to scope
        ast.doTag(null);

        // assert that the scoped object is resolved to 
        // the expected object
        assertEquals(cmpInstance, ast.getScopedObject());

        if(cmpInstance instanceof BaseName){
            assertTrue(((BaseName)cmpInstance).getScope().contains(theme));
        }
        else if(cmpInstance instanceof Association){
            assertTrue(((Association)cmpInstance).getScope().contains(theme));
        }
        else if(cmpInstance instanceof Occurrence){
            assertTrue(((Occurrence)cmpInstance).getScope().contains(theme));
        }
        
        // assert that the body was not called
        assertFalse(scriptWasCalled);

    }

 


    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        tm = getTopicMapFromResource(TestData.TM_TEST2_XTM,
                "some.org/");

        ast = new AddScopeTag();
        ctx = new JellyContext();
        ast.setContext(ctx);

        ctx.setVariable(Dictionary.KEY_TOPICMAP, tm);


    }

}